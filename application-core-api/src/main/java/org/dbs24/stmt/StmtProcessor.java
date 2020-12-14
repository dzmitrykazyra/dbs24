/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.stmt;

import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.nullsafe.ObjectBuilder;
import org.dbs24.application.core.nullsafe.StackTraceInfo;
import org.dbs24.consts.SysConst;
import static org.dbs24.consts.SysConst.FORMAT_dd_MM_yyyy;
import static org.dbs24.consts.SysConst.NOT_DEFINED;
import reactor.core.publisher.Mono;

@Log4j2
public final class StmtProcessor<T> {

    private Boolean silent = SysConst.BOOLEAN_FALSE;
    private T artifact;
    private FinallyProcessor<T> finallyProcessor;

    //==========================================================================
    public StmtProcessor(VoidStmt<T> voidStmt) {
        processStmt(voidStmt);
    }

    //==========================================================================
    public StmtProcessor(Stmt<T> stmt) {
        artifact = processStmt(stmt);
    }

    //==========================================================================
    public StmtProcessor(Stmt<T> stmt, Boolean silent) {
        this.silent = silent;
        artifact = processStmt(stmt);
    }

    //==========================================================================
    public StmtProcessor(VoidStmt<T> stmt, Boolean silent) {
        this.silent = silent;
        processStmt(stmt);
    }

    //==========================================================================
    public StmtProcessor(Class<T> stmtClass, NewObjectProcessor<T> newObjectProcessor) {
        artifact = processStmtClass(stmtClass, newObjectProcessor);
    }

    //==========================================================================
    public static <T> void execute(VoidStmt<T> voidStmt) {
        new StmtProcessor(voidStmt);
    }

    //==========================================================================
    public static <T> void executeSilent(Stmt<T> stmt) {
        new StmtProcessor(stmt, SysConst.BOOLEAN_TRUE);
    }

    //==========================================================================
    public static <T> void executeSilent(VoidStmt<T> voidStmt) {
        new StmtProcessor(voidStmt, SysConst.BOOLEAN_TRUE);
    }

    //==========================================================================
    public static <T> StmtProcessor createSilent(Stmt<T> stmt) {
        return new StmtProcessor(stmt, SysConst.BOOLEAN_TRUE);
    }

    //==========================================================================
    public static <T> void create(VoidStmt<T> voidStmt) {
        final StmtProcessor stmtProcessor = new StmtProcessor(voidStmt);
        //stmtProcessor.silent = SysConst.BOOLEAN_TRUE;        
    }

    //==========================================================================
    public static <T> T create(Stmt<T> stmt) {
        return (T) (new StmtProcessor(stmt)).get();
    }

    //==========================================================================
    public static <T> T create(Class<T> stmtClass, NewObjectProcessor<T> newObjectProcessor) {
        return (T) (new StmtProcessor(stmtClass, newObjectProcessor)).get();
    }

    //==========================================================================
    public static <T> T create(Class<T> stmtClass) {

        final NewObjectProcessor<T> newObjectProcessor = (object) -> {
        };

        return (T) (new StmtProcessor(stmtClass, newObjectProcessor)).get();
    }

    //==========================================================================
    public static <T, V> T create(
            final Class<T> clazz,
            final V constructorArg) {
        return StmtProcessor.<T, V>create(clazz, constructorArg, null);
    }

    //==========================================================================
    public static <T, V1, V2> T create(
            final Class<T> clazz,
            final V1 constructorArg1,
            final V2 constructorArg2) {
        return StmtProcessor.<T, V1, V2>create(clazz, constructorArg1, constructorArg2, null);
    }

    //==========================================================================
    public static <T, V> T create(
            final Class<T> clazz,
            final V constructorArg,
            final NewObjectProcessor<T> newObjectProcessor) {

        return StmtProcessor.create(() -> {

            final Class[] cArgList = new Class[1];
            cArgList[0] = constructorArg.getClass();
            final T newObject = clazz.getDeclaredConstructor(cArgList).newInstance(constructorArg);
            if (StmtProcessor.notNull(newObjectProcessor)) {
                newObjectProcessor.initialize(newObject);
            }
            return newObject;
        });
    }
    //==========================================================================

    public static <T, V1, V2> T create(
            final Class<T> clazz,
            final V1 constructorArg1,
            final V2 constructorArg2,
            final NewObjectProcessor<T> newObjectProcessor) {

        return StmtProcessor.create(() -> {

            final Class[] cArgList = new Class[2];
            cArgList[0] = constructorArg1.getClass();
            cArgList[1] = constructorArg2.getClass();
            final T newObject = clazz.getDeclaredConstructor(cArgList).newInstance(constructorArg1, constructorArg2);
            if (StmtProcessor.notNull(newObjectProcessor)) {
                newObjectProcessor.initialize(newObject);
            }
            return newObject;
        });
    }

    //==========================================================================
    private void processStmt(VoidStmt<T> stmt) {
        try {
            stmt.executeStmt();
        } catch (Throwable t) {
            processException(t);
        }
    }

    //==========================================================================
    private T processStmt(Stmt<T> stmt) {
        try {
            artifact = stmt.executeStmt();
        } catch (Throwable t) {
            processException(t);
        }

        return artifact;
    }

    //==========================================================================
    private T processStmtClass(Class<T> stmtClass, NewObjectProcessor<T> newObjectProcessor) {
        try {
            artifact = stmtClass.newInstance();

            newObjectProcessor.initialize(artifact);

        } catch (Throwable t) {
            processException(t);
        }

        return artifact;
    }

    //==========================================================================
    public <T> T get() {
        return (T) artifact;
    }

    //==========================================================================
    public StmtProcessor whenIsNull(Stmt<T> stmt) {
        try {
            if (null == artifact) {
                artifact = stmt.executeStmt();
            }
        } catch (Throwable t) {
            this.processException(t);
        }

        return this;
    }

    //==========================================================================
    private void processException(Throwable t) {

        if (!silent) {
            t.printStackTrace();
            final String errMsg = getErrorMessage(t);
            log.error("### {}: {}", t.getClass().getSimpleName(), errMsg);
            throw new InternalAppException(errMsg);
        } else {
            log.warn("silent exception {}: {}", t.getClass().getSimpleName(), t.getLocalizedMessage());
        }
    }

    //==========================================================================
    public static final String getErrorMessage(Throwable th) {
        return InternalAppException.getExtendedErrMessage(th);
    }

    public static final String getStackTraceRaw(Throwable th) {
        return new StackTraceInfo(th).getStringStackTraceInfo();
    }
    //==========================================================================

    public static String getObj2String(Object object) {

        return (String) StmtProcessor.createSilent(() -> object)
                .whenIsNull(() -> ((LocalDate) object).format(FORMAT_dd_MM_yyyy))
                .whenIsNull(() -> String.format("%s", object))
                .whenIsNull(() -> String.format("%d", object))
                .whenIsNull(() -> String.format("%f", object))
                .whenIsNull(() -> String.format("%b", object))
                .whenIsNull(() -> NOT_DEFINED)
                .get();
    }

    //==========================================================================
    public static Thread runNewThread(
            final Runnable target) {
        final Thread thread = new Thread(target);
        thread.start();
        return thread;
    }

    //==========================================================================
    public static final Boolean isNull(Object object) {
        return !StmtProcessor.notNull(object);
    }

    public static final Boolean notNull(Object object) {
        return (null != object);
    }

    public static final Object nvl(Object object, Object defObject) {
        return (null != object ? object : defObject);
    }
}
