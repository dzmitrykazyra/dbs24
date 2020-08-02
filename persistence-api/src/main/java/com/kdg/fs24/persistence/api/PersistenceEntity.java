/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.persistence.api;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.sysconst.SysConst;
import java.io.Serializable;

/**
 *
 * @author Козыро Дмитрий
 */
public interface PersistenceEntity extends Serializable, Cloneable {

    public default Boolean justCreated() {
        return SysConst.BOOLEAN_FALSE;
    }
}
