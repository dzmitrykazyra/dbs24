package org.dbs24.proxy.core.importer;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ProxyService;
import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyProvider;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PS_ACTUAL;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyTypeEnum.PT_STATIC;

@Component
@Log4j2
public class StaticProxyImporter extends AbstractApplicationService {
    @Value("${config.proxy.static.folder}")
    private String pathToFolder;

    @Value("${config.proxy.static.delete}")
    private Boolean isNeedDeleteAfterImport;

    private final ReferenceService referenceService;
    private final ProxyService proxyService;

    @Autowired
    public StaticProxyImporter(ReferenceService referenceService, ProxyService proxyService) {
        this.referenceService = referenceService;
        this.proxyService = proxyService;
    }

//    @Scheduled(fixedRateString = "${config.proxy.static.refresh}")
    public void uploadStaticProxies() {
        log.info("Start uploading static proxies");

        StmtProcessor.execute(() -> {

            final DirectoryStream<Path> dirsStream = Files.newDirectoryStream(Paths.get(pathToFolder));

            dirsStream.forEach(file -> {

                log.debug("Process file - {}", file.getFileName());

                final String filenameWithoutExtension = file.toFile().getName().replaceAll("\\.[a-z]+", "");

                StmtProcessor.execute(() -> Files.readAllLines(file).forEach(row -> {

                    final Proxy proxy = proxyService.getProxyDao().saveProxy(extractProxyFromFileRow(row, filenameWithoutExtension));

                    log.info("Proxy with id = {} created/updated", proxy.getProxyId());

                    StmtProcessor.ifTrue(isNeedDeleteAfterImport, () -> {
                        log.info("Delete file: {}", file);

                        Files.delete(file);
                    });
                }));
            });
            dirsStream.close();
        });
    }

    /**
     * Method allows split file row and find or create proxy
     *
     * @param row should match: <b>[url:port:login:pass:countryIso]</b>
     * @return new or updated {@link Proxy}
     */
    private Proxy extractProxyFromFileRow(String row, String filename) {
        final String[] proxyInfo = row.split(":");

        final String url = proxyInfo[0];
        final Integer port = Integer.valueOf(proxyInfo[1]);
        final String login = proxyInfo[2];
        final String password = proxyInfo[3];
        final String countryIso = proxyInfo[4];

        final Proxy proxy = proxyService.findProxy(url, port).orElseGet(proxyService::createProxy);

        fillProxyInfo(proxy, url, port, login, password, filename, countryIso);

        return proxy;
    }

    private ProxyProvider extractProxyProviderFromFilename(String filename) {
        String providerName = filename.substring(0, filename.indexOf("_"));

        return referenceService.findProxyProviderByName(providerName);
    }

    private LocalDateTime extractExpiredDateFromFilename(String filename) {
        String date = filename.substring(filename.indexOf("_") + 1);

        final LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd_MM_yyyy"));

        return LocalDateTime.of(localDate, LocalTime.now());
    }

    private void fillProxyInfo(Proxy proxy, String url,
                               Integer port, String login,
                               String password, String filename,
                               String countryIso) {

        proxy.setUrl(url);
        proxy.setSocksPort(port);
        proxy.setLogin(login);
        proxy.setPass(password);
        proxy.setProxyStatus(referenceService.findProxyStatus(PS_ACTUAL));
        proxy.setCountry(referenceService.findCountryByIso(countryIso.toUpperCase()));
        proxy.setProxyType(referenceService.findProxyType(PT_STATIC.getCode()));
        proxy.setDateBegin(LocalDateTime.now());
        proxy.setDateEnd(extractExpiredDateFromFilename(filename));
        proxy.setActualDate(LocalDateTime.now());
        proxy.setProxyProvider(extractProxyProviderFromFilename(filename));
    }

}
