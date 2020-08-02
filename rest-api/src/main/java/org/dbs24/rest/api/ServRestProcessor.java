/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

//import org.dbs24.jdbc.service.api.AbstractJdbcService;
//import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.log.LogService;
//import org.dbs24.services.api.ServiceLocator;
import org.dbs24.application.core.nullsafe.NullSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
//import org.dbs24.entity.core.api.ActionEntity;

/**
 *
 * @author Козыро Дмитрий
 */
public abstract class ServRestProcessor extends RestProcessor {

    public ServRestProcessor() {
        super();
    }

    @POST
    @Consumes({HttpConst.APP_JSON, HttpConst.APP_XML})
    @Path(LogService.REGISTER_REQ)
    // зарегистрировать рест-запрос
    public Response registerRequest(final String reqList) {

        return NullSafe.create(LogService.getCurrentObjProcName(LogService.getCurrentObjProcName(this)))
                .execute2result(() -> {

                    //this.registerRequestsExt(this.processValueString(paramlist));
                    final String json = this.processValueString(reqList);

                    return this.processRequest(
                            ReqInfo.class,
                            reqList.hashCode(),
                            json,
                            () -> {

                                // регистрируем в отдельном потоке
                                NullSafe.runNewThread(() -> {

                                    // коллекция для подтверждения
                                    final Collection<ReqInfo> confCollection = this.<ReqInfo>getObjectListFromJson(json, ReqInfo.class);

//                                    // сохраняем в БД
//                                    ServiceLocator
//                                            .find(AbstractJdbcService.class)
//                                            .createCallBath("{call log_insert_log_requests(:RT, :ADDR, :MD, :JS, :ANS, :EM, :DR)}")
//                                            .execBatch(stmt -> {
//
//                                                synchronized (REQUESTS_LIST) {
//                                                    // чистим начальный список
//
//                                                    confCollection
//                                                            .stream()
//                                                            .unordered()
//                                                            .forEach((reqInfo) -> {
//
//                                                                REQUESTS_LIST.removeIf((removeItem -> {
//
//                                                                    final Boolean isMatch = (removeItem.getReqHashCode() == reqInfo.getReqHashCode());
//
//                                                                    if (isMatch) {
//
//                                                                        stmt.setParamByName("RT", reqInfo.getReqType());
//                                                                        stmt.setParamByName("ADDR", reqInfo.getReqAddress());
//                                                                        stmt.setParamByName("MD", removeItem.getReqDateTime());
//                                                                        stmt.setParamByName("JS", removeItem.getReqJson());
//                                                                        stmt.setParamByName("ANS", removeItem.getReqAnswer());
//                                                                        stmt.setParamByName("EM", reqInfo.getReqException());
//                                                                        stmt.setParamByName("DR", reqInfo.getReqDuration());
//                                                                        //stmt.setParamByName("EN", reqInfo.get);
//
////                                                                        if (removeItem.getReqClass().equals(LogAuditRecord.class)) {
////
////                                                                        }
//                                                                        stmt.addBatch();
//
//                                                                        reqInfo.setReqClass(removeItem.getReqClass());
//                                                                        reqInfo.setReqJson(removeItem.getReqJson());
//
//                                                                    }
//
//                                                                    return isMatch;
//                                                                }));
//                                                            });
//                                                }
//                                            }).commit();
//
                                    // выделяем entity из сообщения
                                    // сообщения об ошибках
                                });

                                return Response
                                        .ok(HttpConst.HTTP_200_OK_STRING)
                                        .entity(HttpConst.HTTP_200_OK_STRING)
                                        .build();
                            });
                }).<Response>getObject();
    }

//    @POST
//    @Consumes({HttpConst.APP_JSON, HttpConst.APP_XML})
//    @Path(LogService.REG_PARAM)
//    // вернуть настроечный параметр
//
//    public Response getRegistryParam(final String paramlist) {
//
//        return NullSafe.create(LogService.getCurrentObjProcName(LogService.getCurrentObjProcName(this)))
//                .execute2result(() -> {
//
//                    //this.registerRequestsExt(this.processValueString(paramlist));
//                    final String json = this.processValueString(paramlist);
//
//                    return this.processRequest(
//                            Map.class,
//                            paramlist.hashCode(),
//                            json,
//                            () -> {
//
//                                final Map.Entry<String, String> entry = this
//                                        .getMapFromJson(json)
//                                        .entrySet()
//                                        .iterator()
//                                        .next();
//
//                                return Response
//                                        .ok("Ку")
//                                        .entity(this.getRegParam(entry.getKey(), entry.getValue()))
//                                        .build();
//                            });
//                }).<Response>getObject();
//    }

    @POST
    @Consumes({HttpConst.APP_JSON, HttpConst.APP_XML})
    @Path(LogService.PATH_4PING)
    public Response receivePingMessage(final String pingMessage) {

        return NullSafe.create(LogService.getCurrentObjProcName(LogService.getCurrentObjProcName(this)))
                .execute2result(() -> {

                    return this.processRequest(
                            Object.class,
                            pingMessage.hashCode(),
                            pingMessage,
                            () -> {
                                return Response
                                        .ok()
                                        .entity(String.format("connection is successfull \n {'%s', %s, %s} ",
                                                pingMessage,
                                                this.getClass().getCanonicalName(),
                                                LocalDateTime.now()))
                                        .build();
                            });
                }).<Response>getObject();
    }
}
