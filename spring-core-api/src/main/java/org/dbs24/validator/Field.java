/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import org.dbs24.spring.core.api.RefEnum;

public enum Field implements RefEnum {

    CORE_ACTION1("field.general", 1),
    CORE_ACTION2("field1", 100),
    CORE_ACTION3("field2", 200),
    GENERAL_FIELD("fld.general", 1000),
    COUNTRY("fld.country", 2000),
    CURRENCY("fld.currency", 3000),    
    //Proxies
    ALG_ID("fld.algId", 100000),
    PROXY_ID("fld.proxyId", 100001),
    COUNTRY_NAME("fld.countryName", 100002),
    PROXY_PROVIDER_NAME("fld.proxyProviderName", 100003),
    PROXY_TYPE_NAME("fld.proxyTypeName", 10004),
    PROXY_AMOUNT("fld.amount", 10005),
    PROXY_REQUEST_ID("fld.requestId", 10006),
    ALG_UNKNOWN("usage.unknown", 200000),
    //Applications
    APPLICATION_STATUS_ID("fld.applicationStatusId", 200000),
    APPLICATION_ID("fld.applicationId", 200001),
    APPLICATION_NAME("fld.applicationName", 200002),
    APPLICATION_NETWORK_NAME("fld.applicationNetworkName", 200003),
    // Contracts
    CONTRACT_ID("cntr.contract_id", 300001),
    CONTRACT_DATE("cntr.contract_date", 300011),
    BEGIN_DATE("cntr.begin_date", 300012),
    END_DATE("cntr.end_date", 300013),
    SUBSRIPTION_AMOUNT("cntr.subscription.amount", 300014),
    CONTRACT_TYPE("cntr.type", 300015),
    MODIFY_REASON("cntr.modify.reason", 300016),
    MODIFY_REASON_NOTE("cntr.modify.note", 300017),
    DAYS_AMOUNT("cntr.days.amount", 300018),
    // Payments
    PAYMENT_DATE("pmt.pmt_date", 400001),
    PACKAGE("pmt.package", 400002),
    LOGIN_TOKEN("pmt.login_token", 400003),
    PLATFORM("pmt.login_token", 400004),
    PAYMENT_SERVICE("pmt.service", 400005),
    PAYMENT_STATUS("pmt.status", 400006),
    PAYMENT_SUMM("pmt.summ", 400007),
    PAYMENT_SUMM_EQU("pmt.summ_equ", 400008),
    APPLY_ORIGINAL_TRANSACTION_ID("ios.transaction.id", 400010),
    GOOGLE_PURCHASE_TOKEN("android.transaction.id", 400020),
    // Tariffs
    TARIFF_PLAN_STATUS("tariff.plan.status", 500001),
    TARIFF_SUBSCRIPTION_AMOUNT("tariff.plan.subscriptions.amount", 500010),
    TARIFF_DURATION("tariff.plan.duration", 500020),
    TARIFF_DEVICE_TYPE("tariff.plan.device.type", 500030),
    TARIFF_SKU("tariff.plan.sku", 500040),
    TARIFF_CONTRACT_TYPE("tariff.plan.contract.type", 500050);


    //==========================================================================
    private final int code;
    private final String value;

    Field(String value, int code) {
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
}
