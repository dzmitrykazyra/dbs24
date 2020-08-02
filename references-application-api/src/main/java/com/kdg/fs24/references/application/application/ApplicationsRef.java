/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.application.application;

/**
 *
 * @author kazyra_d
 */
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.references.core.AbstractReference;
import com.kdg.fs24.exception.references.ReferenceNotFound;
import com.kdg.fs24.references.api.LangStrValue;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import java.util.Collection;

// справочник приложений
public class ApplicationsRef<T extends Application> extends AbstractReference<Application> {

    //==========================================================================
    public T getApplicationById(final Integer app_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findApplicationById(app_id)),
                String.format("Неизвестный код приложения (ApplicationsRef.app_id=%d)", app_id));

    }

    //==========================================================================
    public String getAppUrlById(final Integer app_id) throws ReferenceNotFound {

        return ((T) this.<T>findReference(() -> (this.findAppUrlById(app_id)),
                String.format("Неизвестный код приложения (ApplicationsRef.app_id=%d)", app_id))).getApp_url();
    }

    //==========================================================================
    public String getAppNameById(final Integer app_id) throws ReferenceNotFound {
        return getApplicationById(app_id).getApp_name();
    }

    //==========================================================================
    public String getAppNameByCode(final String app_code) throws ReferenceNotFound {

        return ((T) this.<T>findReference(() -> (this.findAppNameByCode(app_code)),
                String.format("Неизвестный код приложения (ApplicationsRef.app_code=%s)", app_code))).getApp_name();
    }

    //==========================================================================
    public String getAppUrlByCode(final String app_code) throws ReferenceNotFound {

        return ((T) this.<T>findReference(() -> (this.findAppUrlByCode(app_code.toUpperCase())),
                String.format("Неизвестный код приложения (ApplicationsRef.app_code=%s)", app_code))).getApp_url();
    }

    //==========================================================================
    private T findApplicationById(final Integer app_id) {

        return (T) this.findCachedRecords((object) -> ((T) object).getApp_id().equals(app_id));

    }

    //==========================================================================
    private String findAppNameByCode(final String app_code) {

        String appName = null;
        T application = (T) this.findCachedRecords((object) -> ((Application) object).getApp_code().equals(app_code));

        if (NullSafe.notNull(application)) {
            appName = application.getApp_name();
        }

        return appName;

    }

    //==========================================================================
    private String findAppUrlByCode(final String app_code) {

        String appName = null;
        Application application = (Application) this.findCachedRecords((object) -> ((Application) object).getApp_code().equals(app_code));

        if (NullSafe.notNull(application)) {
            appName = application.getApp_name();
        }

        return appName;
    }

    //==========================================================================
    private String findAppUrlById(final Integer app_id) {

        String appUrl = null;
        Application application = (Application) this.findCachedRecords((object) -> ((Application) object).getApp_id().equals(app_id));

        if (NullSafe.notNull(application)) {
            appUrl = application.getApp_name();
        }

        return appUrl;
    }

    //==========================================================================
    public static <T extends Application> void registerReference() {
//        AbstractReference.<T>store(() -> {
//
//            final Class<T> clazz = (Class<T>) (Application.class);
//
//            return NullSafe.create()
//                    .execute2result(() -> {
//
//                        final Collection<T> refCollection = ServiceFuncs.<T>createCollection();
//
//                        refCollection.add((T) NullSafe.createObject(clazz)
//                                .setApp_id(100)
//                                .setApp_code("LOAN.100")
//                                .setApp_name(AbstractReference.getTranslatedValue(
//                                        new LangStrValue("n-a", "APP_NAME")))
//                                .setApp_url("www.blas-bla")
//                        );
//                        refCollection.add((T) NullSafe.createObject(clazz)
//                                .setApp_id(1001)
//                                .setApp_code("USERS")
//                                .setApp_name(AbstractReference.getTranslatedValue(
//                                        new LangStrValue("n-a", "Приложение юзерс")))
//                                .setApp_url("security-manager/user.xhtml")
//                        );
//                        refCollection.add((T) NullSafe.createObject(clazz)
//                                .setApp_id(102)
//                                .setApp_code("lc.fl")
//                                .setApp_name(AbstractReference.getTranslatedValue(
//                                        new LangStrValue("n-a", "Кредиты подозрительным лицам")))
//                                .setApp_url("retail-loan-contracts/Loan2Individual.xhtml")
//                        );
//                        refCollection.add((T) NullSafe.createObject(clazz)
//                                .setApp_id(1010)
//                                .setApp_code("APP.13")
//                                .setApp_name(AbstractReference.getTranslatedValue(
//                                        new LangStrValue("n-a", "Кредиты физическим лицам")))
//                                .setApp_url("retail-loan-contracts/loans2fl")
//                        );
//
//                        return refCollection;
//                    }).<Collection<T>>getObject();
//
//        }, "{call core_insertorupdate_applicationsref(:APP, :ACODE, :ANAME, :URL)}",
//                (stmt, record) -> {
//
//                    stmt.setParamByName("APP", record.getApp_id())
//                            .setParamByName("ACODE", record.getApp_code())
//                            .setParamByName("ANAME", record.getApp_name())
//                            .setParamByName("URL", record.getApp_url());
//                });
        //======================================================================

//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (1002, 'ROLE', 'Роли', 'security-manager/role.xhtml');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (101, 'APP.113', 'Ценные бумаги', 'securities');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (1100, 'RUNAPP', 'Форма запуска приложений', 'security-manager/run-application-form.xhtml');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (10012, 'ROLES', 'Пользовательская роль', 'security-manager/role.xhtml');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (102, 'lc.fl', 'Кредиты подозрительным лицам', 'retail-loan-contracts/Loan2Individual.xhtml');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (105, 'LOAN.MASSOPER', 'Массовые операции', 'retail-loan-contracts/massopers-form.xhtml');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (1010, 'APP.13', 'Кредиты физическим лицам', 'retail-loan-contracts/гы-гы');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (104, 'app.113.4', 'Кредиты нефизическим лицам', 'retail-loan-contracts/bla-bla-bla.xhtml');
//INSERT INTO core_applicationsref (app_id, app_code, app_name, app_url) 
//	VALUES (100013, 'LOAN.113', 'Массовые операции', 'retail-loan-contracts/massopers-form.xhtml');
    }

}
