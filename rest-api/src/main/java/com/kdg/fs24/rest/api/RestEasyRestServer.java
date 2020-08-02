/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.rest.api;

/**
 *
 * @author kazyra_d
 */
import com.kdg.fs24.application.core.service.funcs.GenericFuncs;
import com.sun.net.httpserver.*;
import org.jboss.resteasy.plugins.server.sun.http.HttpContextBuilder;
//import org.jboss.resteasy.plugins.server.sun.http.ResteasyHttpHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.kdg.rwsst.remote.exception.RSE_UnknownRestEasy_Exc;

import java.net.InetSocketAddress;
import java.util.Arrays;
import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.nullsafe.NullSafe;

public abstract class RestEasyRestServer<T extends RestProcessor> implements RestServer {

    private HttpServer httpServer;
    //private ResteasyHttpHandler httpServer;
    private HttpContextBuilder contextBuilder;

    final private Class<T> restProcessorClazz;

    public RestEasyRestServer() {
        //this.restProcessor = restProcessor;
        super();
        this.restProcessorClazz = (Class<T>) GenericFuncs.<T>getTypeParameterClass(this.getClass());
        LogService.LogInfo(this.getClass(), () -> String.format("initialize rest service '%s'", restProcessorClazz.getCanonicalName()));
    }

    @Override
    public void start(final int socketAddr) {
        //String errMsg = SysConst.EMPTY_STRING;
        //log.trace("try to start rest service");

        //Smt2ts.setRemoteCommand(remoteCommand);
        //final int socketAddr = Property.getAsIntOrDefault("remote.rest.server.port", 8070);
        //final int socketAddr = 2019;
        NullSafe.create(this.httpServer)
                .whenIsNull(() -> {
//                    LogService.LogInfo(this.getClass(), String.format("try to start rest service '%s:%d'",
//                            restProcessorClazz.getCanonicalName(),
//                            socketAddr));

                    //LogService.LogInfo(this.getClass(), String.format("using port for rest service : (%d)", socketAddr));
                    httpServer = HttpServer.create(new InetSocketAddress(socketAddr), 10);

                    contextBuilder = NullSafe.createObject(HttpContextBuilder.class);
                    contextBuilder.getDeployment()
                            .getActualResourceClasses()
                            .addAll(Arrays.asList(this.restProcessorClazz));
                    contextBuilder.bind(httpServer);
                    httpServer.start();

                });
    }

    @Override
    public void stop() {
        NullSafe.create(this.contextBuilder)
                .safeExecute(() -> {
                    this.contextBuilder.cleanup();
                    this.httpServer.stop(0);
                });
    }
}
