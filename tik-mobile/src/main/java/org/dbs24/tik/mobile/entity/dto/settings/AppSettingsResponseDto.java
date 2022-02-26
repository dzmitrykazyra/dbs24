package org.dbs24.tik.mobile.entity.dto.settings;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
public class AppSettingsResponseDto {

    private Integer appSettingsId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime actualDate;

    private String packageName;
    private String companyName;
    private String appName;
    private String minVersionCode;
    private String minVersion;
    private String siteUrl;
    private String email;
    private String whatsappId;
    private String note;

    public static AppSettingsResponseDto of(AppSettings appSettings) {

        return AppSettingsResponseDto.builder()
                .withAppSettingsId(appSettings.getAppSettingsId())
                .withActualDate(appSettings.getActualDate())
                .withPackageName(appSettings.getPackageName())
                .withCompanyName(appSettings.getCompanyName())
                .withAppName(appSettings.getAppName())
                .withMinVersionCode(appSettings.getMinVersionCode())
                .withMinVersion(appSettings.getMinVersion())
                .withSiteUrl(appSettings.getSiteUrl())
                .withEmail(appSettings.getEmail())
                .withWhatsappId(appSettings.getWhatsappId())
                .withNote(appSettings.getNote())
                .build();
    }
}