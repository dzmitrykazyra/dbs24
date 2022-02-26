/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.consts;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import org.dbs24.proxy.core.entity.domain.*;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.spring.core.api.RefEnum;
import org.springframework.security.core.parameters.P;

public final class ProxyConsts {

    public static class PkgConsts {

        public static final String PROXY_PACKAGE = "org.dbs24.proxy.core";
        public static final String PROXY_PACKAGE_REPO = PROXY_PACKAGE + ".repo";
    }

    public enum ErrorTypeEnum {
        ET_BAN("ET.BAN", 1),
        ET_OTHER("ET.OTHER", 2);

        @Getter
        private final int code;
        @Getter
        private final String value;

        ErrorTypeEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public static final Collection<ErrorType> USAGE_ERROR_TYPES = ServiceFuncs.
                <ErrorType>createCollection(
                cp -> Arrays
                        .stream(ErrorTypeEnum.values())
                        .map(stringRow -> StmtProcessor.create(ErrorType.class, record -> {
                            record.setErrorTypeId(stringRow.getCode());
                            record.setErrorTypeName(stringRow.getValue());
                        }))
                        .forEach(cp::add)
        );
    }

    // Types
    public enum ProxyTypeEnum {

        PT_STATIC("PT.STATIC", 1),
        PT_RESIDENTIAL("PT.RESIDENTIAL", 2),
        PT_MOBILE("PT.MOBILE", 3),
        PT_OWN_MOBILE("PT.OWN_MOBILE", 4);

        public static final Collection<ProxyType> PROXY_TYPES = ServiceFuncs.<ProxyType>createCollection(cp -> Arrays.stream(ProxyTypeEnum.values())
                .map(stringRow -> StmtProcessor.create(ProxyType.class, record -> {
                    record.setProxyTypeId(stringRow.getCode());
                    record.setProxyTypeName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> PROXY_TYPES_IDS = ServiceFuncs.createCollection(cp -> PROXY_TYPES.stream().map(t -> t.getProxyTypeId()).forEach(ref -> cp.add(ref)));

        private final int code;
        private final String value;

        ProxyTypeEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }
    }

    public enum ApplicationStatusEnum {

        AS_ACTIVE("AS_ACTIVE", 1),
        AS_DISABLED("AS_DISABLED", 2);

        @Getter
        private final int code;
        @Getter
        private final String value;

        ApplicationStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public static final Collection<ApplicationStatus> APPLICATION_STATUSES = ServiceFuncs.
                <ApplicationStatus>createCollection(
                status -> Arrays
                        .stream(ApplicationStatusEnum.values())
                        .map(stringRow -> StmtProcessor.create(ApplicationStatus.class, record -> {
                            record.setApplicationStatusId(stringRow.getCode());
                            record.setApplicationStatusName(stringRow.getValue());
                        }))
                        .forEach(status::add)
        );
    }

    // Statuses
    public enum ProxyStatusEnum implements RefEnum {

        PS_ACTUAL("PS_ACTUAL", 1),
        PS_EXPRIRED("PS_EXPRIRED", 2),
        PS_CANCELLED("PS_CANCELLED", 3),
        PS_OUT_OF_TRAFFIC("PS_OUT_OF_TRAFFIC", 4);

        public static final Collection<ProxyStatus> PROXY_STATUSES = ServiceFuncs.<ProxyStatus>createCollection(cp -> Arrays.stream(ProxyStatusEnum.values())
                .map(stringRow -> StmtProcessor.create(ProxyStatus.class, record -> {
                    record.setProxyStatusId(stringRow.getCode());
                    record.setProxyStatusName(stringRow.getValue());
                })).forEach(cp::add)
        );
        public static final Collection<Integer> PROXY_STATUSES_IDS = ServiceFuncs.createCollection(cp -> PROXY_STATUSES.stream().map(t -> t.getProxyStatusId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        ProxyStatusEnum(String value, Integer code) {
            this.value = value;
            this.code = code;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public Integer getCode() {
            return code;
        }

//        public ProxyStatusEnum findByCode(Integer code) {
//            return (ProxyStatusEnum) findByCode(ProxyStatusEnum.values(), code);
//        }f
    }

    // Types
    public enum ProxyProviderEnum {

        PP_ASTRO("AstroProxy", "Astro", 1),
        PP_AIR("AirProxy", "Air", 2),
        PP_LINE("ProxyLine", "Line", 3),
        PP_WINS("ProxyWins", "Wins", 4),
        PP_ZETA("ZetaSol", "Zeta", 5);

        public static final Collection<ProxyProvider> PROXY_PROVIDERS = ServiceFuncs.<ProxyProvider>createCollection(cp -> Arrays.stream(ProxyProviderEnum.values())
                .map(stringRow -> StmtProcessor.create(ProxyProvider.class, record -> {
                    record.setProviderId(stringRow.getId());
                    record.setProviderCode(stringRow.getCode());
                    record.setProviderName(stringRow.getValue());
                })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> PROVIDERS_IDS = ServiceFuncs.createCollection(cp -> PROXY_PROVIDERS.stream().map(t -> t.getProviderId()).forEach(ref -> cp.add(ref)));

        private final Integer id;
        private final String code;
        private final String value;

        ProxyProviderEnum(String value, String code, Integer id) {
            this.value = value;
            this.code = code;
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public String getCode() {
            return code;
        }

        public Integer getId() {
            return id;
        }
    }

    // Proxy alg selection
    public enum AlgSelectionEnum {

        ALG_MIN_USAGE(10, "usage.min"),
        ALG_UNKNOWN(20, "usage.unknown"),
        ALG_LONGEST_NOT_USED(30, "usage.longest.not.used"),
        ALG_NEWEST_ADDED(40, "added.newest");

        public static final Collection<AlgSelection> PROXY_ALG_SELECTIONS = ServiceFuncs.<AlgSelection>createCollection(r -> Arrays.stream(AlgSelectionEnum.values())
                .map(stringRow -> StmtProcessor.create(AlgSelection.class, record -> {
                    record.setAlgId(stringRow.getCode());
                    record.setAlgName(stringRow.getValue());
                })).forEach(r::add)
        );

        public static final Collection<Integer> PROXY_ALG_SELECTIONS_IDS = ServiceFuncs.createCollection(r -> PROXY_ALG_SELECTIONS.stream().map(t -> t.getAlgId()).forEach(ref -> r.add(ref)));
        //======================================================================
        private final int code;
        private final String value;

        AlgSelectionEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }

        public boolean isEqualByProxyRequest(ProxyRequest proxyRequest) {
            return proxyRequest.getAlgSelection().getAlgName()
                    .equals(this.getValue());
        }
    }

    // Proxy socks auth methods
    public enum SocksAuthMethods {

        SAM_NO_AUTH(0, "NO_AUTH METHOD"),
        SAM_USER_PASS(2, "USER_PASS METHOD"),
        MULTI_USER_PASS(3, "MULTI_USER_PASS METHOD"),
        IPV4_PASS(4, "IPv4_PASS METHOD"),
        IPV6_PASS(5, "IPv6_PASS METHOD");

        public static final Collection<SocksAuthMethod> SOCKS_AUTH_METHOD = ServiceFuncs.<SocksAuthMethod>createCollection(r -> Arrays.stream(SocksAuthMethods.values())
                .map(stringRow -> StmtProcessor.create(SocksAuthMethod.class, record -> {
                    record.setSocksAuthMethodId(stringRow.getCode());
                    record.setSocksAuthMethodName(stringRow.getValue());
                })).forEach(r::add));

        public static final Collection<Integer> SOCKS_AUTH_METHOD_IDS = ServiceFuncs.createCollection(r -> SOCKS_AUTH_METHOD.stream().map(t -> t.getSocksAuthMethodId()).forEach(ref -> r.add(ref)));
        //======================================================================
        private final int code;
        private final String value;

        SocksAuthMethods(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }
    }

    // Countries
    public static class Countries {

        // initialize from applications.yml
        public static final Collection<Integer> COUNTRIES_IDS = ServiceFuncs.<Integer>createCollection();
    }

    // Caches
    public static class Caches {

        public static final String CACHE_PROXY = "proxies";
        public static final String CACHE_PROXY_OPTIONAL = "proxiesOpt";
        public static final String CACHE_PROXY_BY_ADDRESS = "proxiesByAddress";
        public static final String CACHE_ACTUAL_PROXIES = "actualProxies";
        public static final String CACHE_PROXY_STATUS = "ProxyStatusRef";
        public static final String CACHE_COUNTRY = "proxyCountriesRef";
        public static final String CACHE_COUNTRY_NAME = "proxyCountriesRefByName";
        public static final String CACHE_COUNTRY_ISO = "proxyCountriesRefByIso";
        public static final String CACHE_COUNTRY_OPTIONAL = "proxyCountriesRefOptional";
        public static final String CACHE_PROXY_TYPE = "proxyTypeRef";
        public static final String CACHE_PROXY_PROVIDER = "proxyProviderRef";
        public static final String CACHE_PROXY_PROVIDER_OPTIONAL = "proxyProviderRefOptional";
        public static final String CACHE_SOCKS_AUTH_METHOD = "socksAuthMethodsRefOptional";
        public static final String CACHE_ALG_SELECTION = "proxyAlgSelectionRef";
        public static final String CACHE_ACTUAL_PROXY_USAGE = "actualProxyUsage";
        public static final String CACHE_PROXY_USAGE = "proxyUsage";


    }

    //==========================================================================
    // Messages templated
    public static class ErrMsg {

        public static final String FIELD_NOT_FOUND = "mandatory field is not defined - ";
        public static final String INVALID_FIELD_VALUE = "invalid field value - ";

    }
}
