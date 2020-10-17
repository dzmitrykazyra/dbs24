/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.fields.desc;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.references.api.AbstractCardFile;
//import org.dbs24.services.FS24JdbcService;
//import org.dbs24.services.api.ServiceLocator;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
// список переопределенных полей сущностей
public class AppFieldsCaptions extends AbstractCardFile {

    public AppFieldsCaptions() {
        super();
    }

    @Override
    protected void loadReference(final Boolean needReload, final Long app_id) {

        // подзагрузили в кеш
        if (needReload) {

//            this.setCardFiles(
//                    ServiceLocator
//                            .find(FS24JdbcService.class)
//                            .createQuery("SELECT * FROM core_AppFieldsCaptions WHERE app_id = :APP ORDER BY user_id DESC ")
//                            .setParamByName("APP", app_id.intValue())
//                            .<AppFieldCaption>createCollection(AppFieldCaption.class));

        }
    }

    //==========================================================================
    private AppFieldCaption getActualAppFieldCaption(final Long user_id, final String fieldName) {

        AppFieldCaption appFieldCaption =  ServiceFuncs.<AppFieldCaption>getCollectionElement_silent(this.getCardFiles(),
                p -> p.getUser_id().equals(user_id) && p.getField_name().equals(fieldName));
        
        if (NullSafe.isNull(appFieldCaption)) {
            appFieldCaption =  ServiceFuncs.<AppFieldCaption>getCollectionElement_silent(this.getCardFiles(),
                p -> p.getUser_id().equals(SERVICE_USER_ID) && p.getField_name().equals(fieldName));
        }

        return appFieldCaption;
        
//        List<AppFieldCaption> afc = ((List<AppFieldCaption>) this.getCardFiles()).stream()
//                .filter(p -> p.getUser_id().equals(user_id) && p.getField_name().equals(fieldName)).collect(Collectors.toList());
//
//        if (afc.isEmpty()) {
//            afc = ((List<AppFieldCaption>) this.getCardFiles()).stream()
//                    .filter(p -> p.getUser_id().equals(SERVICE_USER_ID) && p.getField_name().equals(fieldName)).collect(Collectors.toList());
//        }
//
//        return afc.get(0);
    }

    //==========================================================================
    public String getAttrCaption(final Long user_id, final String fieldName) {

        return getActualAppFieldCaption(user_id, fieldName).getField_caption();
    }

    //==========================================================================
    public String getAttrToolTip(final Long user_id, final String fieldName) {

        return getActualAppFieldCaption(user_id, fieldName).getField_tooltip();
    }

}
