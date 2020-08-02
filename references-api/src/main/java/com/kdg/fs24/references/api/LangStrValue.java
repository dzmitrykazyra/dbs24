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
public class LangStrValue {

    private String en;
    private String ru;

    public LangStrValue(final String en, final String ru) {
        this.en = en;
        this.ru = ru;
    }

    //==========================================================================
    public String getEn() {
        return en;
    }

    public void setEn(final String en) {
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(final String ru) {
        this.ru = ru;
    }

}
