package org.dbs24.google.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.google.api.dto.GmailAccountInfo;
import org.dbs24.google.api.dto.ProxyInfo;
import org.dbs24.kafka.api.KafkaMessage;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
public class ExecOrderAction extends KafkaMessage {

    @EqualsAndHashCode.Include
    /** Useful only for app-promo microservice */
    private Integer actionId;

    /** Useful only for app-promo microservice */
    private Integer providerId;

    /** Useful only for app-promo microservice */
    private Integer orderId;

    /** Useful only for app-promo microservice */
    private Long startExecution;

    /** Task reference id field {@link org.dbs24.google.api.OrderActionsConsts.ActionEnum} */
    private Integer actRefId;

    /**
     * Word or sentence to simulate real user search
     * only getting target application(application with required package) top 1 in search list
     */
    private String keyWordToSearch;

    /** Stars quantity to rate content */
    private Integer starsQuantity;

    /** Flag content reference value field {@link org.dbs24.google.api.FlagContentReason} */
    private String flagContentName;
    private String comment;
    private String appPackage;
    private String commentIdToRate;
    private Boolean isCommentHelpful;

    /** Account information (tokens, email, pass, phone config name) */
    private GmailAccountInfo gmailAccountInfo;

    /** Proxy information (url, port, login, pass) */
    private ProxyInfo proxyInfo;
}