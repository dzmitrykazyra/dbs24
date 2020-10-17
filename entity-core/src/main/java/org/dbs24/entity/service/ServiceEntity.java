/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.service;

import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.core.api.ActionClassesCollectionLink;
import org.dbs24.entity.core.api.IsSingleInstance;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityTypeId;
//import org.dbs24.exception.api.CreateEntityException;
import org.dbs24.fields.desc.FieldDescriptionImpl;
import static org.dbs24.application.core.sysconst.SysConst.*;
import java.util.Collection;

/**
 *
 * @author kazyra_d
 */
@IsSingleInstance
@DefaultEntityStatus(entity_status = ES_VALID)
@EntityTypeId(entity_type_id = 500, entity_type_name = "Сервисная энтити")
//@ActionClassesCollectionLink(collection_service = ServiceActionClassesService.class)
public class ServiceEntity { // extends AbstractActionEntity {

    private Collection<FieldDescriptionImpl> settings;
    private Integer appId;
    private Long userId;

    //==========================================================================
    // конструктор по умолчанию
    public ServiceEntity()  {
        super();
    }

    //==========================================================================    
//    @Override
//    public void saveEntityInstance() {
//        // нет записи в БД
//    }

    //==========================================================================
    public Collection<FieldDescriptionImpl> getSettings() {
        return settings;
    }

    public void setSettings(final Collection<FieldDescriptionImpl> settings) {
        this.settings = settings;
    }

    //==========================================================================
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(final Integer appId) {
        this.appId = appId;
    }

    //==========================================================================    
//    public Long getUserId() {
//        if (userId.equals(LONG_ZERO)) {
//            this.userId = AbstractActionEntity.getCurrentUserId();
//        }
//        return userId;
//    }
    public void setUserId(final Long userId) {
        this.userId = userId;
    }
    //==========================================================================    
}
