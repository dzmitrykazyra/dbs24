package org.dbs24.tik.mobile.entity.dto.firebase;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.FireBaseApplication;

@Data
public class FireBaseApplicationInfoDto {

    private Integer firebaseAppId;
    private Long actualDate;
    private String adminSdk;
    private String packageName;
    private String name;
    private String dbUrl;
    private Boolean isActual;

    public static FireBaseApplicationInfoDto of(FireBaseApplication fireBaseApplication) {

        return StmtProcessor.create(FireBaseApplicationInfoDto.class, dto -> {

            dto.setActualDate(NLS.localDateTime2long(fireBaseApplication.getActualDate()));
            dto.setAdminSdk(fireBaseApplication.getAdminSdk());
            dto.setDbUrl(fireBaseApplication.getDbUrl());
            dto.setFirebaseAppId(fireBaseApplication.getFirebaseAppId());
            dto.setIsActual(fireBaseApplication.getIsActual());
            dto.setName(fireBaseApplication.getName());
            dto.setPackageName(fireBaseApplication.getPackageName());
        });
    }

}
