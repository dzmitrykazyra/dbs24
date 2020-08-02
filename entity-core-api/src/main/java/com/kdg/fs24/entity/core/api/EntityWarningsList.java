/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.core.api;

/**
 *
 * @author Козыро Дмитрий
 */
public interface EntityWarningsList {

    int getWarningsCount();

    void clearWarnings();

    String get_field_warning(String fldName);

}
