/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.log;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.consts.SysConst.*;
//import org.apache.logging.log4j.LogManager;

import java.util.Map;

//import org.apache.log4j.Logger;
//import org.apache.logging.log4j.Logger;
/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public abstract class LogService {

    public static final String PATH_REST = "/restApi";
    public static final String PATH_LOG = "/logMessage";
    public static final String PATH_AUDIT = "/logAudit";
    public static final String PATH_THROWABLE = "/logThrowable";
    public static final String PATH_LOG_ERR = "/logERR";
    public static final String PATH_4PING = "/4ping";
    public static final String REGISTER_REQ = "/regRequest";
    public static final String SERV_MAIL = "/4serviceMail";
    //==========================================================================
    //==========================================================================
    private final static String msgMask = EMPTY_STRING
            //            .concat("//=========================================================\n")
            .concat(" %s");
//            .concat("//=========================================================\n");

    private final static String msgMask2 = EMPTY_STRING
            //.concat("//=========================================================\n")
            .concat(" %s: %s");
//            .concat("//=========================================================\n");

    public static final void LogInfo( Class clazz, InfoMessage infoMessage) {
//        if (TestConst.TEST_MODE_RUNNING) {
        new Thread(() -> {
            synchronized (LogService.class) {

                //LogManager.getLogger(clazz).info(infoMessage.getMessage());

            }
        }).start();
//        }
    }

    //--------------------------------------------------------------------------
    public static final void LogInfo( Class clazz, String procedureName, InfoMessage infoMessage) {
//        if (TestConst.TEST_MODE_RUNNING) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).info(infoMessage.getMessage());
            }
        }).start();
//        }
    }

    //--------------------------------------------------------------------------
    public static final void LogWarn( Class clazz, InfoMessage infoMessage) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).warn(infoMessage.getMessage());
            }
        }).start();
    }

    //--------------------------------------------------------------------------
    public static final void LogDebug( Class clazz, InfoMessage infoMessage) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).debug(infoMessage.getMessage());
            }
        }).start();
    }

    //--------------------------------------------------------------------------
    public static final void LogWarn( Class clazz, String procedureName, InfoMessage infoMessage) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).warn(infoMessage.getMessage());
            }
        }).start();
    }
    //--------------------------------------------------------------------------

    public static final void LogErr( Class clazz, InfoMessage infoMessage) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).error(infoMessage.getMessage());
            }
        }).start();
    }

    //--------------------------------------------------------------------------
    public static final void LogErr( Class clazz, String procedureName, InfoMessage infoMessage) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).error(infoMessage.getMessage());
            }
        }).start();
    }

    //--------------------------------------------------------------------------
    public static final void LogErr( Class clazz, String procedureName, Throwable th) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).error(procedureName, th);
            }
        }).start();
    }

    //--------------------------------------------------------------------------
    public static final void LogErr( Class clazz, Throwable th) {
        new Thread(() -> {
            synchronized (LogService.class) {
                //LogManager.getLogger(clazz).error("???", th);
            }
        }).start();

    }
    //--------------------------------------------------------------------------

    public static final String getCurrentProcName() {
        return Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName();
    }

    //--------------------------------------------------------------------------
    public static final String getCurrentObjProcName( Object o) {
        return LogService.getCurrentObjProcName(o, -1);
    }

    //--------------------------------------------------------------------------
    public static final String getCurrentObjProcName( Object o, int shift) {
        return String.format("%s.%s",
                o.getClass().getName(),
                Thread.currentThread()
                        .getStackTrace()[2 - shift]
                        .getMethodName());
    }
    //--------------------------------------------------------------------------

    public static final String getCurrentObjProcName( Class clazz) {
        return String.format("%s.%s", clazz.getName(), Thread.currentThread()
                .getStackTrace()[2]
                .getMethodName());
    }
    //--------------------------------------------------------------------------

    private static String warPackageName;

    public static String getWarPackageName() {

        if (null == warPackageName) {
            warPackageName = LogService.getModuleName(LogService.class.
                    getProtectionDomain().
                    getCodeSource().
                    getLocation().
                    getFile());
        }

        return warPackageName;
    }

    public static String getWarPackageName( Class clazz) {

        return LogService.getModuleName(clazz.
                getProtectionDomain().
                getCodeSource().
                getLocation().
                getFile());

    }

    private static String getModuleName( String classUrl) {
        String moduleName = EMPTY_STRING;
        int indexOff = classUrl.lastIndexOf("/WEB-INF");
        if (indexOff > 0) {
            String url = classUrl.substring(0, classUrl.lastIndexOf("/WEB-INF"));
            while (moduleName.lastIndexOf(".") < 0) {
                if (!moduleName.isEmpty()) {
                    url = url.substring(0, url.lastIndexOf("/WEB-INF"));
                }
                moduleName = url.substring(url.lastIndexOf("/") + 1);
            }
        } else {
            //LogService.LogWarn(LogService.class, String.format("bad classUrl (%s)", classUrl));
            moduleName = ".";
        }
        return moduleName.substring(0, moduleName.lastIndexOf("."));
    }

}
