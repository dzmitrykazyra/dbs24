/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.ad.server.dto.AdSettingDetails;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AppSettingsInfo {

    private String contactEmail;
    private String contactTelegram;
    private String contactWhatsApp;

    private String requiredAppVersion;
    private String requiredVersionCode;
    private Long serverTimestampMillis;
    private String primaryPaymentAppPackageName;
    private String secondaryPaymentAppPackageName;
    private Boolean shouldDownloadProxy;
    private Boolean shouldDownloadAds;
    private String proxyActualVersion;
    private String note;
    @EqualsAndHashCode.Include
    private String companyName;
    @EqualsAndHashCode.Include
    private String appName;
    @EqualsAndHashCode.Include
    private String siteUrl;
    private AdSettingDetails adSettingDetails;
    private Integer minSubscriptionLivenessTime;
}
