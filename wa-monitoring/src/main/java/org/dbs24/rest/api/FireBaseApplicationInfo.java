/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.FireBaseApplication;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FireBaseApplicationInfo {

    @EqualsAndHashCode.Include
    private Integer firebaseAppId;
    private Long actualDate;
    private String adminSdk;
    private String packageName;
    private String name;
    private String dbUrl;
    private Boolean isActual;

    public void assign(FireBaseApplication fireBaseApplication) {
        setActualDate(NLS.localDateTime2long(fireBaseApplication.getActualDate()));
        setAdminSdk(fireBaseApplication.getAdminSdk());
        setDbUrl(fireBaseApplication.getDbUrl());
        setFirebaseAppId(fireBaseApplication.getFirebaseAppId());
        setIsActual(fireBaseApplication.getIsActual());
        setName(fireBaseApplication.getName());
        setPackageName(fireBaseApplication.getPackageName());
    }
}
