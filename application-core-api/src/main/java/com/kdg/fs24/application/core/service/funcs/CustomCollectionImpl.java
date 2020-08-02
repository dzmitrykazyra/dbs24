/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.service.funcs;

import com.kdg.fs24.application.core.sysconst.SysConst;
//import com.kdg.fs24.application.core.log.LogService;

/**
 *
 * @author kazyra_d
 */
public final class CustomCollectionImpl implements CustomCollection {

    private String collectionRecord = SysConst.NOT_DEFINED;

    public CustomCollectionImpl(final String initialHeader) {
        super();
        this.collectionRecord = initialHeader;
    }

    @Override
    public void addCustomRecord(final CustomRecord customRecord) {
        collectionRecord = collectionRecord.concat(customRecord.getRecord());
    }

    @Override
    @Deprecated
    public void printRecord() {
        //LogService.LogInfo(this.getClass(), collectionRecord);
    }

    @Override
    public String getRecord() {
        return collectionRecord;
    }
}
