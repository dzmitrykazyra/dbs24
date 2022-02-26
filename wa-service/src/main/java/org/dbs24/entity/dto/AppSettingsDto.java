package org.dbs24.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AppSettingsDto {

    private Long actualDate;
    private String packageName;
    private String email;
    private String minVersionCode;
    private String minVersion;
    private String companyName;
    private String appName;
    private String siteUrl;
    private String whatsappId;
    private String note;

}
