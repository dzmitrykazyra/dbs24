/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.consts;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.card.pmt.entity.Application;
import org.dbs24.card.pmt.entity.PaymentService;
import org.dbs24.card.pmt.entity.PaymentStatus;
import org.dbs24.stmt.StmtProcessor;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.consts.RestHttpConsts.URI_API;

public final class CardPaymentConsts {

    // Package consts
    public static class PkgConsts {

        public static final String PROXY_PACKAGE = "org.dbs24.card.pmt";
        public static final String PROXY_PACKAGE_REPO = PROXY_PACKAGE + ".repo";
    }

    // URI
    public static class UriConsts {

        // Proxy
        public static final String URI_CREATE_OR_UPDATE_PAYMENT = URI_API + "/createOrUpdatePayment";
        public static final String URI_CREATE_OR_UPDATE_WA_PAYMENT = URI_API + "/createOrUpdateWhatsappTrackerPayment";
        public static final String URI_CANCEL_WA_PAYMENT = URI_API + "/cancelWhatsappTrackerPayment";
    }

    public static class RestQueryParams {

        public static final String QP_PROXY_ID = "proxyId";
        public static final String QP_TOKEN = "token";
    }

    // URI
    public static class Platforms {

        // Proxy
        public static final String PTF_IOS = "Ios";
        public static final String PTF_ANDROID = "Android";
    }

    // Applications
    public enum ApplicationEnum {

        APP_WA_TRACKER("WA_TRACKER", 10),
        APP_TIK_ASSIST("TIK_ASSIST", 20),
        APP_INSTA_FS("INSTA_FS", 30);

        public static final Collection<Application> APPLICATIONS_LIST = ServiceFuncs.<Application>createCollection(cp -> Arrays.stream(ApplicationEnum.values())
                .map(stringRow -> StmtProcessor.create(Application.class, record -> {
            record.setApplicationId(stringRow.getCode());
            record.setApplicationName(stringRow.getValue());
        })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> APPLICATIONS_LIST_IDS = ServiceFuncs.createCollection(cp -> APPLICATIONS_LIST.stream().map(t -> t.getApplicationId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        ApplicationEnum(String value, Integer code) {
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

    // PaymentService
    public enum PaymentServiceEnum {

        PS_GOOGLE_PAY("GOOGLE_PAY", 100),
        PS_APPLE_PAY("APPLE_PAY", 200);
//        PS_STRIP("PS_STRIP", 1000),
//        PS_SWIFT("PS_SWIFT", 1100);

        public static final Collection<PaymentService> PAYMENT_SERVICES_LIST = ServiceFuncs.<PaymentService>createCollection(cp -> Arrays.stream(PaymentServiceEnum.values())
                .map(stringRow -> StmtProcessor.create(PaymentService.class, record -> {
            record.setPaymentServiceId(stringRow.getCode());
            record.setPaymentServiceName(stringRow.getValue());
        })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> PAYMENT_SERVICES_LIST_IDS = ServiceFuncs.createCollection(cp -> PAYMENT_SERVICES_LIST.stream().map(t -> t.getPaymentServiceId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        PaymentServiceEnum(String value, Integer code) {
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

    // PaymentService
    public enum PaymentStatusEnum {

        PS_ACTUAL("Actual", 100),
        PS_CANCELLED("PS_CANCELLED", 110);

        public static final Collection<PaymentStatus> PAYMENT_STATUSES_LIST = ServiceFuncs.<PaymentStatus>createCollection(cp -> Arrays.stream(PaymentStatusEnum.values())
                .map(stringRow -> StmtProcessor.create(PaymentStatus.class, record -> {
            record.setPaymentStatusId(stringRow.getCode());
            record.setPaymentStatusName(stringRow.getValue());
        })).forEach(ref -> cp.add(ref))
        );

        public static final Collection<Integer> PAYMENT_STATUSES_LIST_IDS = ServiceFuncs.createCollection(cp -> PAYMENT_STATUSES_LIST.stream().map(t -> t.getPaymentStatusId()).forEach(ref -> cp.add(ref)));

        private final Integer code;
        private final String value;

        PaymentStatusEnum(String value, Integer code) {
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
        public static final Collection<String> COUNTRIES_IDS = ServiceFuncs.<String>createCollection();
    }

    // Currencies
    public static class Currencies {

        // initialize from applications.yml
        public static final Collection<String> CURRENCIES_IDS = ServiceFuncs.<String>createCollection();
    }

    // Caches
    public static class Caches {

        public static final String CACHE_COUNTRY = "countries";
        public static final String CACHE_APPLICATION = "applications";
        public static final String CACHE_CURRENCIES = "currencies";
        public static final String CACHE_PAYMENT_SERVICE = "paymentService";
        public static final String CACHE_PAYMENT_STATUS = "paymentStatus";

    }

    //==========================================================================
    // Messages templated
    public static class ErrMsg {

        public static final String FIELD_NOT_FOUND = "mandatory field is not defined - ";
        public static final String INVALID_FIELD_VALUE = "invalid field value - ";

    }
}
