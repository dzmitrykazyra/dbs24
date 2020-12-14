/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import static org.dbs24.consts.SysConst.*;
//import org.dbs24.application.core.log.LogService;

/**
 *
 * @author kazyra_d
 */
public final class CustomCollectionImpl implements CustomCollection {

    private String collectionRecord = NOT_DEFINED;

    public CustomCollectionImpl( String initialHeader) {
        super();
        this.collectionRecord = initialHeader;
    }

    @Override
    public void addCustomRecord( CustomRecord customRecord) {
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
