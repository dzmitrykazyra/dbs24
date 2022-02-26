package org.dbs24.tik.mobile.entity.dto.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.tik.mobile.entity.domain.AppSettings;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class AppSettingsRequestDto {

    private String packageName;
    private String companyName;
    private String appName;
    private String minVersionCode;
    private String minVersion;
    private String siteUrl;
    private String email;
    private String whatsappId;
    private String note;
}