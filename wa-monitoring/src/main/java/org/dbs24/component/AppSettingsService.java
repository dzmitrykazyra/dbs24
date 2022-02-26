/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.AppSettings;
import org.dbs24.entity.AppSettingsHist;
import org.dbs24.entity.PackageDetails;
import org.dbs24.entity.dto.AdsSettingsDto;
import org.dbs24.entity.dto.AppSettingsDto;
import org.dbs24.repository.AppSettingsHistRepository;
import org.dbs24.repository.AppSettingsRepository;
import org.dbs24.repository.PackageDetailsRepository;
import org.dbs24.rest.api.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.lang.String.format;
import static org.dbs24.consts.SysConst.STRING_NULL;
import static org.dbs24.consts.WaConsts.Classes.APP_SETTINGS_INFO_CLASS;
import static org.dbs24.consts.WaConsts.References.isAndroid;
import static org.dbs24.consts.WaConsts.References.isIos;
import static org.dbs24.consts.WaConsts.RestQueryParams.QP_PACKAGE_NAME;
import static org.dbs24.stmt.StmtProcessor.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.client.WebClient.builder;
import static reactor.core.publisher.Mono.just;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
@EqualsAndHashCode(callSuper = true)
public class AppSettingsService extends AbstractApplicationService {

    @Value("${config.license.company:Company/developer name}")
    private String kwCompany;

    @Value("${config.license.appName:App Name}")
    private String kwAppName;

    @Value("${config.license.contacts:App Contact informations}")
    private String kwContacts;

    @Value("${config.settings.contact-telegram:watracker_support}")
    private String contactTelegram;

    @Value("${config.settings.secondary-payment-app-packageName:com.tinyapp.smartpurchase}")
    private String secondaryPaymentAppPackageName;

    @Value("${config.settings.should-download-proxy:false}")
    private Boolean shouldDownloadProxy;

    @Value("${config.settings.should-download-ads:false}")
    private Boolean shouldDownloadAds;

    @Value("${config.settings.proxy-actual-version:dev}")
    private String proxyActualVersion;

    @Value("${config.wa.ads.url}")
    private String adsUrl;

    @Value("${config.wa.ads.api.settings}")
    private String adsApiSettings;

    @Value("${config.wa.subscription.min-liveness-time:600000}")
    private Integer minSubscriptionLivenessTime;

    final PackageDetailsRepository packageDetailsRepository;
    final AppSettingsRepository appSettingsRepository;
    final AppSettingsHistRepository appSettingsHistRepository;

    private WebClient adsWebClient;

    public AppSettingsService(PackageDetailsRepository packageDetailsRepository, AppSettingsRepository appSettingsRepository, AppSettingsHistRepository appSettingsHistRepository) {
        this.packageDetailsRepository = packageDetailsRepository;
        this.appSettingsRepository = appSettingsRepository;
        this.appSettingsHistRepository = appSettingsHistRepository;
    }

    @Override
    public void initialize() {
        super.initialize();

        StmtProcessor.assertNotNull(String.class, adsUrl, "parameter ${config.wa.ads.url}'");

        adsWebClient = setupSSL(builder(), adsUrl)
                .baseUrl(adsUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();

        log.debug("adsWebClient: successfully created ({})", adsUrl);

    }

    //==========================================================================
    public Optional<AppSettings> findAppSettings(String packageName) {
        return appSettingsRepository.findByPackageName(packageName);
    }

    public Collection<AppSettings> findAllAppSettings() {
        return appSettingsRepository.findAll();
    }

    @FunctionalInterface
    interface AppSettingsHistBuilder {
        void buildAppSettingsHist(AppSettings appSettings);
    }

    final BiFunction<AppSettingsDto, AppSettings, AppSettings> assignDto = (appSettingsDto, appSettings) -> {

        appSettings.setEmail(appSettingsDto.getEmail());
        appSettings.setMinVersion(appSettingsDto.getMinVersion());
        appSettings.setMinVersionCode(appSettingsDto.getMinVersionCode());
        appSettings.setPackageName(appSettingsDto.getPackageName());
        appSettings.setNote(appSettingsDto.getNote());
        appSettings.setAppName(appSettingsDto.getAppName());
        appSettings.setCompanyName(appSettingsDto.getCompanyName());
        appSettings.setSiteUrl(appSettingsDto.getSiteUrl());
        appSettings.setWhatsappId(appSettingsDto.getWhatsappId());

        return appSettings;
    };

    final BiFunction<AppSettingsDto, AppSettingsService.AppSettingsHistBuilder, AppSettings> assignAppSettingsInfo = (appSettingsDto, appSettingsHistBuilder) -> {

        final AppSettings appSettings = findAppSettings(appSettingsDto.getPackageName())
                .orElseGet(getNewAppSetting());

        // store history
        StmtProcessor.ifNotNull(appSettings.getAppSettingId(), () -> appSettingsHistBuilder.buildAppSettingsHist(appSettings));

        StmtProcessor.ifNotNull(appSettingsDto.getActualDate(), ad -> appSettings.setActualDate(NLS.long2LocalDateTime(ad)),
                () -> appSettings.setActualDate(LocalDateTime.now()));

        assignDto.apply(appSettingsDto, appSettings);

        return appSettings;
    };

    @Transactional
    public CreatedAppSetting createOrUpdateAppSettings(AppSettingsDto appSettingsDto) {

        return create(CreatedAppSetting.class, createdAppSetting -> {

            log.debug("create/update appSetting: {}", appSettingsDto);

            StmtProcessor.assertNotNull(String.class, appSettingsDto.getPackageName(), "packageName name is not defined");

            final AppSettings appSettings = findOrCreateAppSettings(appSettingsDto, settings -> saveAppSettingsHist(createAppSettingsHist(settings)));

            final Boolean isNewSetting = StmtProcessor.isNull(appSettings.getAppSettingId());

            appSettingsRepository.save(appSettings);

            final String finalMessage = format("Setting is %s (appSettingId=%d, packageName='%s')",
                    isNewSetting ? "created" : "updated",
                    appSettings.getAppSettingId(), appSettings.getPackageName());

            createdAppSetting.setSettingName(finalMessage);

            log.debug(finalMessage);

        });
    }

    //==========================================================================
    @Transactional
    @Deprecated
    public CreatedPackageDetails createOrUpdatePackageDetails(PackageDetailsInfo packageDetailsInfo) {

        return create(CreatedPackageDetails.class, ca -> {

            ca.setPackageName("endpoint was deprecated");

            log.debug("created/update packageName: '{}'", ca.getPackageName());

        });
    }

    //==========================================================================
    public Mono<AppSettingsInfo> getSettings(ServerRequest serverRequest) {

        final String packageName = serverRequest.queryParam(QP_PACKAGE_NAME).orElseGet(() -> STRING_NULL);

        return just(create(APP_SETTINGS_INFO_CLASS, appSettingsInfo -> {

            log.debug("getting app settings for package '{}'", packageName);

            ifNull(packageName, () -> appSettingsInfo.setPrimaryPaymentAppPackageName("package variable not defined"), () ->

                    findAppSettings(packageName).ifPresentOrElse(appSettings -> {

                        appSettingsInfo.setContactEmail(appSettings.getEmail());
                        appSettingsInfo.setContactTelegram(contactTelegram);
                        appSettingsInfo.setContactWhatsApp(appSettings.getWhatsappId());
                        appSettingsInfo.setRequiredAppVersion(appSettings.getMinVersion());
                        appSettingsInfo.setRequiredVersionCode(appSettings.getMinVersionCode());
                        appSettingsInfo.setServerTimestampMillis(NLS.localDateTime2long(LocalDateTime.now()));
                        appSettingsInfo.setPrimaryPaymentAppPackageName(appSettings.getPackageName());
                        appSettingsInfo.setSecondaryPaymentAppPackageName(secondaryPaymentAppPackageName);
                        appSettingsInfo.setShouldDownloadProxy(shouldDownloadProxy);
                        appSettingsInfo.setShouldDownloadAds(shouldDownloadAds);
                        appSettingsInfo.setProxyActualVersion(proxyActualVersion);
                        appSettingsInfo.setCompanyName(appSettings.getCompanyName());
                        appSettingsInfo.setAppName(appSettings.getAppName());
                        appSettingsInfo.setSiteUrl(appSettings.getSiteUrl());
                        appSettingsInfo.setNote(appSettings.getNote());
                        appSettingsInfo.setMinSubscriptionLivenessTime(minSubscriptionLivenessTime);

                    }, () -> appSettingsInfo.setPrimaryPaymentAppPackageName(format("Unknown package - '%s'", packageName))));

        })).flatMap(appSettingsInfo -> adsWebClient
                .get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(adsApiSettings)
                        .queryParam(QP_PACKAGE_NAME, packageName)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AdsSettingsDto.class)
                .onErrorResume(throwable -> just(create(AdsSettingsDto.class, adsSettingsDto -> adsSettingsDto.setAdSettingDetails(null /*throwable.getMessage()*/))))
                .flatMap(adsSettingsDto -> {

                    log.debug("adsSettingsDto: {}", adsSettingsDto);

                    appSettingsInfo.setAdSettingDetails(adsSettingsDto.getAdSettingDetails());

                    log.debug("return settings: {}", appSettingsInfo);

                    return just(appSettingsInfo);
                }));
    }

    //==========================================================================
    public LicenseAgreementInfo getLicenseAgreementInfo(String packageName, Integer deviceTypeId) {

        return create(LicenseAgreementInfo.class, lai -> {

            final AppSettings appSettings = findAppSettings(packageName)
                    .orElseGet(() -> {
                        log.error(" #### getLicenseAgreementInfo: package '{}' not found", packageName);

                        return create(AppSettings.class, pd -> {

                            pd.setAppName("WATracker");
                            pd.setCompanyName("wa-Solution LLC");
                            pd.setEmail("wa-solution@gmail.com");

                        });
                    });

            StmtProcessor.ifNotNull(deviceTypeId, dt -> ifTrue(isAndroid.test(dt) || isIos.test(dt),
                            () -> lai.setLicenseInfo((isAndroid.test(dt) ? googleLicenseTemplateInfo : iosLicenseTemplateInfo)
                                    .replaceAll(kwCompany, appSettings.getCompanyName())
                                    .replaceAll(kwAppName, appSettings.getAppName())
                                    .replaceAll(kwContacts, appSettings.getEmail())),
                            () -> lai.setLicenseInfo(format("Unknown deviceTypeId: %d", deviceTypeId))),
                    () -> lai.setLicenseInfo("deviceType is null in query param"));
        });
    }

    //==========================================================================
    public AllPackageDetailsInfo getAllPackageDetails() {

        return create(AllPackageDetailsInfo.class,
                afa -> findAllAppSettings()
                        .forEach(app -> afa.getPackages().add(create(PackageDetailsInfo.class,
                                pdi -> {
                                    pdi.setContactInfo(app.getEmail());
                                    pdi.setCompanyName(app.getCompanyName());
                                    pdi.setActualDate(NLS.localDateTime2long(app.getActualDate()));
                                    pdi.setPackageName(app.getPackageName());
                                    pdi.setAppName(app.getAppName());
                                }))));
    }

    @Deprecated
    //==========================================================================
    public PackageDetails createPackageDetails() {
        return create(PackageDetails.class, a -> a.setActualDate(LocalDateTime.now()));
    }

    @Deprecated
    public PackageDetails findOrCreatePackageDetails(String packageName) {
        return findPackageDetails(packageName).orElseGet(this::createPackageDetails);
    }

    @Deprecated
    public Optional<PackageDetails> findPackageDetails(String packageName) {

        return packageDetailsRepository.findById(packageName);
    }

    @Deprecated
    public Collection<PackageDetails> findAllPackageDetails() {

        return packageDetailsRepository.findAll();
    }

    //==========================================================================

    public AppSettings findOrCreateAppSettings(AppSettingsDto appSettingsDto, AppSettingsService.AppSettingsHistBuilder appSettingsHistBuilder) {
        return assignAppSettingsInfo.apply(appSettingsDto, appSettingsHistBuilder);
    }

    public AppSettings findAppSettings(Integer appSettingId) {

        return appSettingsRepository.findById(appSettingId).orElseThrow();
    }

    private void saveAppSettingsHist(AppSettingsHist appSettingsHist) {
        appSettingsHistRepository.save(appSettingsHist);
    }

    final Supplier<AppSettings> newAppSetting = () -> create(AppSettings.class,
            a -> a.setActualDate(LocalDateTime.now()));

    private AppSettingsHist createAppSettingsHist(AppSettings appSettings) {
        return create(AppSettingsHist.class, ap -> {
            ap.setAppSettingId(appSettings.getAppSettingId());
            ap.setEmail(appSettings.getEmail());
            ap.setActualDate(appSettings.getActualDate());
            ap.setPackageName(appSettings.getPackageName());
            ap.setMinVersion(appSettings.getMinVersion());
            ap.setMinVersionCode(appSettings.getMinVersionCode());
            ap.setNote(appSettings.getNote());
            ap.setCompanyName(appSettings.getCompanyName());
            ap.setAppName(appSettings.getAppName());
            ap.setSiteUrl(appSettings.getSiteUrl());
            ap.setWhatsappId(appSettings.getWhatsappId());
        });
    }

    //==========================================================================
    final String googleLicenseTemplateInfo = "## Privacy Policy\n"
            + "\n"
            + "Company/developer name built the App Name app as a Commercial app. This SERVICE is provided by Company/developer name and is intended for use as is.\n"
            + "\n"
            + "This page is used to inform visitors regarding our policies with the collection, use, and disclosure of Personal Information if anyone decided to use our Service.\n"
            + "\n"
            + "If you choose to use our Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that we collect is used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy.\n"
            + "\n"
            + "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at App Name unless otherwise defined in this Privacy Policy.\n"
            + "\n"
            + "**Information Collection and Use**\n"
            + "\n"
            + "For a better experience, while using our Service, we may require you to provide us with certain personally identifiable information, including but not limited to\n"
            + "\n"
            + "*   Name and version of the operating system\n"
            + "*   Application version\n"
            + "*   Language\n"
            + "*   Country\n"
            + "*   IP address\n"
            + "*   Device vendor and model\n"
            + "*   The information you enter to use the features of application\n"
            + "\n"
            + "The information that we request will be retained by us and used as described in this privacy policy.\n"
            + "The app does use third party services that may collect information used to identify you.\n"
            + "\n"
            + "Link to privacy policy of third party service providers used by the app\n"
            + "\n"
            + "*   [Google Play Services](https://www.google.com/policies/privacy/)\n"
            + "*   [Google Analytics for Firebase](https://firebase.google.com/policies/analytics)\n"
            + "*   [Firebase Crashlytics](https://firebase.google.com/support/privacy/)\n"
            + "\n"
            + "**Log Data**\n"
            + "\n"
            + "We want to inform you that whenever you use our Service, in a case of an error in the app we collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing our Service, the time and date of your use of the Service, and other statistics.\n"
            + "\n"
            + "**Cookies**\n"
            + "\n"
            + "Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.\n"
            + "\n"
            + "This Service does not use these “cookies” explicitly. However, the app may use third party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n"
            + "\n"
            + "**Service Providers**\n"
            + "\n"
            + "We may employ third-party companies and individuals due to the following reasons:\n"
            + "\n"
            + "*   To facilitate our Service;\n"
            + "*   To provide the Service on our behalf;\n"
            + "*   To perform Service-related services; or\n"
            + "*   To assist us in analyzing how our Service is used.\n"
            + "\n"
            + "We want to inform users of this Service that these third parties have access to your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n"
            + "\n"
            + "**Security**\n"
            + "\n"
            + "We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.\n"
            + "\n"
            + "**Links to Other Sites**\n"
            + "\n"
            + "This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by us. Therefore, we strongly advise you to review the Privacy Policy of these websites. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n"
            + "\n"
            + "**Children’s Privacy**\n"
            + "\n"
            + "These Services do not address anyone under the age of 13. We do not knowingly collect personally identifiable information from children under 13\\. In the case we discover that a child under 13 has provided us with personal information, we immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact us so that we will be able to do necessary actions.\n"
            + "\n"
            + "**Changes to This Privacy Policy**\n"
            + "\n"
            + "We may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page. These changes are effective immediately after they are posted on this page.\n"
            + "\n"
            + "**Contact Us**\n"
            + "\n"
            + "If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us at App Contact informations.";

    final String iosLicenseTemplateInfo = "## Privacy Policy\n" +
            "\n" +
            "Company/developer name built the App Name app as a Commercial app. This SERVICE is provided by Company/developer name and is intended for use as is.\n" +
            "\n" +
            "This page is used to inform visitors regarding our policies with the collection, use, and disclosure of Personal Information if anyone decided to use our Service.\n" +
            "\n" +
            "If you choose to use our Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that we collect is used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy.\n" +
            "\n" +
            "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at App Name unless otherwise defined in this Privacy Policy.\n" +
            "\n" +
            "**Information Collection and Use**\n" +
            "\n" +
            "For a better experience, while using our Service, we may require you to provide us with certain personally identifiable information, including but not limited to\n" +
            "\n" +
            "*   Name and version of the operating system\n" +
            "*   Application version\n" +
            "*   Language\n" +
            "*   Country\n" +
            "*   IP address\n" +
            "*   Device vendor and model\n" +
            "*   Device vendor identifier\n" +
            "*   The information you enter to use the features of application\n" +
            "\n" +
            "Our Service collects persistent device vendor identifier to protect it from fraud and provide users with better experience.\n" +
            "\n" +
            "Our Service requests access to Contacts for better user experience to simplify the process of inputting data manually.\n" +
            "\n" +
            "The information that we request will be retained by us and used as described in this privacy policy.\n" +
            "The app does use third party services that may collect information used to identify you.\n" +
            "\n" +
            "Link to privacy policy of third party service providers used by the app\n" +
            "\n" +
            "*   [Google Analytics for Firebase](https://firebase.google.com/policies/analytics)\n" +
            "*   [Firebase Crashlytics](https://firebase.google.com/support/privacy/)\n" +
            "\n" +
            "**Log Data**\n" +
            "\n" +
            "We want to inform you that whenever you use our Service, in a case of an error in the app we collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing our Service, the time and date of your use of the Service, and other statistics.\n" +
            "\n" +
            "**Cookies**\n" +
            "\n" +
            "Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.\n" +
            "\n" +
            "This Service does not use these “cookies” explicitly. However, the app may use third party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n" +
            "\n" +
            "**Service Providers**\n" +
            "\n" +
            "We may employ third-party companies and individuals due to the following reasons:\n" +
            "\n" +
            "*   To facilitate our Service;\n" +
            "*   To provide the Service on our behalf;\n" +
            "*   To perform Service-related services; or\n" +
            "*   To assist us in analyzing how our Service is used.\n" +
            "\n" +
            "We want to inform users of this Service that these third parties have access to your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n" +
            "\n" +
            "**Security**\n" +
            "\n" +
            "We value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.\n" +
            "\n" +
            "**Links to Other Sites**\n" +
            "\n" +
            "This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by us. Therefore, we strongly advise you to review the Privacy Policy of these websites. We have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n" +
            "\n" +
            "**Children’s Privacy**\n" +
            "\n" +
            "These Services do not address anyone under the age of 13. We do not knowingly collect personally identifiable information from children under 13\\. In the case we discover that a child under 13 has provided us with personal information, we immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact us so that we will be able to do necessary actions.\n" +
            "\n" +
            "**Changes to This Privacy Policy**\n" +
            "\n" +
            "We may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. We will notify you of any changes by posting the new Privacy Policy on this page. These changes are effective immediately after they are posted on this page.\n" +
            "\n" +
            "**Contact Us**\n" +
            "\n" +
            "If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us at App Contact informations.\n";

}
