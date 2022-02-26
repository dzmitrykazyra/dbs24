package org.dbs24.google.service.impl;

import com.github.yeriomin.playstoreapi.*;
import com.github.yeriomin.playstoreapi.exception.account.ApiBuilderException;
import com.wrappers.ReloginSupportedClient;
import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.dto.GmailAccountInfo;
import org.dbs24.google.service.ApiInteractService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

@Service
@Log4j2
public class ApiInteractServiceImpl implements ApiInteractService {

    @Override
    public GooglePlayAPIExtended getGPlayApi(ExecOrderAction execOrderAction) throws IOException, ApiBuilderException {

        Properties deviceProperties = StmtProcessor.create(Properties.class,
                properties -> properties.load(ClassLoader.getSystemResourceAsStream(
                        execOrderAction.getGmailAccountInfo().getPhoneConfigName().concat(".properties"))
                )
        );

        PropertiesDeviceInfoProvider deviceInfoProvider = StmtProcessor.create(PropertiesDeviceInfoProvider.class,
                infoProvider -> {
                    infoProvider.setProperties(deviceProperties);
                    infoProvider.setLocaleString(Locale.ENGLISH.toString());
                }
        );

        ReloginSupportedClient reloginSupportedClient = new ReloginSupportedClient(
                execOrderAction.getProxyInfo().getUrl(),
                execOrderAction.getProxyInfo().getPort(),
                execOrderAction.getProxyInfo().getLogin(),
                execOrderAction.getProxyInfo().getPass()
        );

        GooglePlayAPIExtended googlePlayApiExtended = fillGooglePlayApiBuilder(
                execOrderAction.getGmailAccountInfo(),
                reloginSupportedClient,
                deviceInfoProvider
        ).build();

        ((ReloginSupportedClient) googlePlayApiExtended.getClient()).setApi(googlePlayApiExtended);

        return googlePlayApiExtended;
    }

    @Override
    public void updateTokenIfExpired(GooglePlayAPIExtended api, GmailAccountInfo gmailAccountInfo) {

        if (!Objects.equals(api.getTokens().get(GooglePlayAPI.Scope.GPLAY), gmailAccountInfo.getGplayToken())
                || !Objects.equals(api.getTokens().get(GooglePlayAPI.Scope.AASToken), gmailAccountInfo.getAasToken())
                || !Objects.equals(api.getTokens().get(GooglePlayAPI.Scope.GMAIL_V2), gmailAccountInfo.getGmailToken())
                || !Objects.equals(api.getGsfId(), gmailAccountInfo.getGsfId())
        ) {
            gmailAccountInfo.setGplayToken(api.getTokens().get(GooglePlayAPI.Scope.GPLAY));
            gmailAccountInfo.setGmailToken(api.getTokens().get(GooglePlayAPI.Scope.GMAIL_V2));
            gmailAccountInfo.setAasToken(api.getTokens().get(GooglePlayAPI.Scope.AASToken));
            gmailAccountInfo.setGsfId(api.getGsfId());

            log.debug("Update tokens for {} account", gmailAccountInfo.getEmail());

        }
    }

    private PlayStoreApiBuilderExtended fillGooglePlayApiBuilder(GmailAccountInfo gmailAccountInfo,
                                                                 ReloginSupportedClient reloginSupportedClient,
                                                                 PropertiesDeviceInfoProvider deviceInfoProvider) {

        return StmtProcessor.create(PlayStoreApiBuilderExtended.class, builder -> {
            builder.setHttpClient(reloginSupportedClient);
            builder.setDeviceInfoProvider(deviceInfoProvider);
            builder.setEmail(gmailAccountInfo.getEmail());
            builder.setPassword(gmailAccountInfo.getPass());
            builder.setGsfId(gmailAccountInfo.getGsfId());
            builder.setToken(
                    GooglePlayAPI.Scope.AASToken,
                    gmailAccountInfo.getAasToken()
            );
            builder.setToken(
                    GooglePlayAPI.Scope.GMAIL_V2,
                    gmailAccountInfo.getGmailToken()
            );
            builder.setToken(
                    GooglePlayAPI.Scope.GPLAY,
                    gmailAccountInfo.getGplayToken()
            );
        });
    }
}