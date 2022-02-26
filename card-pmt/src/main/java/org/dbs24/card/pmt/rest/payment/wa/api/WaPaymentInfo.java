/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.wa.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.spring.core.api.EntityInfo;

@Data
@EqualsAndHashCode
public class WaPaymentInfo implements EntityInfo {

    private Integer paymentId;
    @JsonProperty("payment_service")
    private Integer paymentServiceId; // 100
    @JsonProperty("payment_date")
    private Long paymentDate; // DDMMYYYY
    @JsonProperty("app_name")
    private String packageName; // package
    @JsonProperty("currency_iso")
    private String currencyIso;
    @JsonProperty("country")
    private String countryCode;
    @JsonProperty("summ")
    private Long paymentSumm;
    @JsonProperty("note")
    private String pmtNote;
    @JsonProperty("platform") // Ios,Android
    private String platform;
    @JsonProperty("login_token")
    private String loginToken;
    @JsonProperty("contract_type_id")
    private Integer contractTypeId;
    @JsonProperty("subs_amount")
    private Integer subsriptionAmount;
    @JsonProperty("end_date")
    private Long endDate;
    @JsonProperty("google_purchase_token")
    private String googlePurchaseToken;
    @JsonProperty("google_order_id")
    private String googleOrderId;
    @JsonProperty("google_sku")
    private String googleSku;
    @JsonProperty("apple_transaction_id")
    private String appleTransactionId;
    @JsonProperty("apple_original_transaction_id")
    private String appleOriginalTransactionId;
    @JsonProperty("apple_product_id")
    private String appleProductId;
}
