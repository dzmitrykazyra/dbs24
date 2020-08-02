/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.rest.api;

import com.kdg.fs24.application.core.service.funcs.GenericFuncs;
import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
//import com.kdg.fs24.mail.core.MailService;
/**
 *
 * @author Козыро Дмитрий
 */
public abstract class AbstractRestService<T extends RestServer> { // implements Service {

    private T restServer;
    //private Class<T> restServerClass;
    //private int serverPort = 1289;

    //==========================================================================
    //==========================================================================
//    @Override
//    public void postInitializeService() {
//        //restServer = new RestEasyRestServer();
//
//        //final ApplicationSetup as = ServiceLocator.find(ApplicationSetup.class);
//        final int defaultPort = 1289;
//        final Integer serverPort = NullSafe.create(ServiceLocator.find(ApplicationSetup.class))
//                .safeExecute((ns_as) -> {
//
////                    LogService.LogInfo(this.getClass(), String.format("getting server port for '%s'  ",
////                            this.getClass().getSimpleName().concat("ServerPort")));
////
////                    LogService.LogInfo(this.getClass(), String.format("getting server port for '%s' (%s) ",
////                            this.getClass().getSimpleName().concat("ServerPort"),
////                            ((ApplicationSetup) ns_as).getRegParam(this.getClass().getSimpleName().concat("ServerPort"), defaultPort)));
//                    return ((ApplicationSetup) ns_as).getRegParam(this.getClass().getSimpleName().concat("ServerPort"), defaultPort);
//                }, defaultPort)
//                .whenIsNull(() -> {
//                    return defaultPort;
//                }).<Integer>getObject();
//
//        // получить номер порта из настроек
//        NullSafe.create(this.restServer)
//                .whenIsNull(() -> {
//                    //this.restServerClass = (Class<T>) AnnotationFuncs.<T>getTypeParameterClass(this.getClass());
//                    this.restServer = NullSafe.createObject(((Class<T>) GenericFuncs.<T>getTypeParameterClass(this.getClass())));
//                    this.restServer.start(serverPort);
//
//                    final String msgOk = String.format("REST service '%s:%d' is ready",
//                            this.getClass().getSimpleName(),
//                            serverPort);
//
//                    LogGate.log4mail(this.getClass(), msgOk, msgOk);
//
//                })
//                .catchException(e -> {
//
//                    final String msgFail = String.format("REST service '%s:%d' is fail (%s)",
//                            this.getClass().getSimpleName(),
//                            serverPort,
//                            NullSafe.getErrorMessage(e)).toUpperCase();
//
//                    LogGate.log4mail(this.getClass(), msgFail, msgFail);
//
//                });
//    }

    //==========================================================================
//    @Override
//    public void stopService() {
//        NullSafe.create(this.restServer)
//                .safeExecute(() -> {
//                    restServer.stop();
//                    final String msgOk = String.format("REST service '%s' is stopped",
//                            this.getClass().getSimpleName());
//
//                    LogGate.log4mail(this.getClass(), msgOk, msgOk);
//                });
//    }
    //==========================================================================
}
