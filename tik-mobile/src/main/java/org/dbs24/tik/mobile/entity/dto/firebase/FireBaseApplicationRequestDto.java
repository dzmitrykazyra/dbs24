package org.dbs24.tik.mobile.entity.dto.firebase;

import lombok.Data;

@Data
public class FireBaseApplicationRequestDto {
    private Integer firebaseAppId;
    private String adminSdk;
    private String packageName;
    private String name;
    private String dbUrl;
    private Boolean isActual;
}
