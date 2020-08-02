/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.api;

/**
 *
 * @author kazyra_d
 */
public enum AppLanguage {

    RUSSIAN(100),
    ENGLISH(200);

    Integer appLanguage;

    AppLanguage(final Integer i) {
        appLanguage = i;
    }

    public Integer getAppLanguageId() {
        return appLanguage;
    }

    public String getName() {
        return this.name();
    }

    public static AppLanguage getEnum(final Integer _id) {
        AppLanguage[] As = AppLanguage.values();
        for (int i = 0; i < As.length; i++) {
            if (_id.equals(As[i].getAppLanguageId())) {
                return As[i];
            }
        }
        return null;
    }

}
