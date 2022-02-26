/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.stmt;

import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.nullsafe.StackTraceInfo;
import org.dbs24.application.core.service.funcs.CollectionProcessor;
import org.dbs24.consts.SysConst;

import java.time.LocalDate;
import java.util.Collection;

import static org.dbs24.consts.SysConst.FORMAT_dd_MM_yyyy;
import static org.dbs24.consts.SysConst.NOT_DEFINED;

public final class StmtProcessor<T> {

    private Boolean silent = SysConst.BOOLEAN_FALSE;
    private T artifact;
    private Throwable throwable;
    private FinallyProcessor<T> finallyProcessor;

    //==========================================================================
    public StmtProcessor(VoidStmt<T> voidStmt) {
        processStmt(voidStmt);
    }

    //==========================================================================
    public StmtProcessor(VoidStmtP1<T> voidStmtP1, T t) {
        processStmt(voidStmtP1, t);
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
    public static <T> void execute(VoidStmtP1<T> voidStmtP1, T t) {
        new StmtProcessor(voidStmtP1, t);
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
    public static <T> StmtProcessor createSilent(VoidStmt<T> stmt) {
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
    public static <T> T createCatch(Stmt<T> stmt, ExceptionProcessor ep) {

        final StmtProcessor<T> sp = StmtProcessor.createSilent(stmt);

        final T t;
        try {
            t = (T) sp.get();
        } finally {
            sp.processException(ep);
        }

        return t;
    }

    //==========================================================================
    public static <T> void createCatch(VoidStmt<T> stmt, ExceptionProcessor ep) {

        StmtProcessor.createSilent(stmt).processException(ep);

    }

    //==========================================================================
    public static <T> T create(Stmt<T> stmt, VoidStmtP1<T> stmt1) {
        final T t = (T) (new StmtProcessor(stmt)).get();
        StmtProcessor.execute(() -> stmt1.executeStmt(t));
        return t;
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
    public static <T, V1, V2, V3> T create(
            final Class<T> clazz,
            final V1 constructorArg1,
            final V2 constructorArg2,
            final V3 constructorArg3) {
        return StmtProcessor.<T, V1, V2, V3>create(clazz, constructorArg1, constructorArg2, constructorArg3, null);
    }

    //==========================================================================
    public static <T, V1, V2, V3, V4> T create(
            final Class<T> clazz,
            final V1 constructorArg1,
            final V2 constructorArg2,
            final V3 constructorArg3,
            final V4 constructorArg4) {
        return StmtProcessor.<T, V1, V2, V3, V4>create(clazz, constructorArg1, constructorArg2, constructorArg3, constructorArg4, null);
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
    public static <T, V1, V2, V3> T create(
            final Class<T> clazz,
            final V1 constructorArg1,
            final V2 constructorArg2,
            final V3 constructorArg3,
            final NewObjectProcessor<T> newObjectProcessor) {

        return StmtProcessor.create(() -> {

            final Class[] cArgList = new Class[3];
            cArgList[0] = constructorArg1.getClass();
            cArgList[1] = constructorArg2.getClass();
            cArgList[2] = constructorArg3.getClass();
            final T newObject = clazz.getDeclaredConstructor(cArgList).newInstance(constructorArg1, constructorArg2, constructorArg3);
            if (StmtProcessor.notNull(newObjectProcessor)) {
                newObjectProcessor.initialize(newObject);
            }
            return newObject;
        });
    }

    //==========================================================================
    public static <T, V1, V2, V3, V4> T create(
            final Class<T> clazz,
            final V1 constructorArg1,
            final V2 constructorArg2,
            final V3 constructorArg3,
            final V4 constructorArg4,
            final NewObjectProcessor<T> newObjectProcessor) {

        return StmtProcessor.create(() -> {

            final Class[] cArgList = new Class[4];
            cArgList[0] = constructorArg1.getClass();
            cArgList[1] = constructorArg2.getClass();
            cArgList[2] = constructorArg3.getClass();
            cArgList[3] = constructorArg4.getClass();
            final T newObject = clazz.getDeclaredConstructor(cArgList).newInstance(constructorArg1, constructorArg2, constructorArg3, constructorArg4);
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
        } catch (Throwable throwable) {
            processException(throwable);
        }
    }

    //==========================================================================
    private void processStmt(VoidStmtP1<T> stmtP1, T t) {
        try {
            stmtP1.executeStmt(t);
        } catch (Throwable throwable) {
            processException(throwable);
        }
    }

    //==========================================================================
    private T processStmt(Stmt<T> stmt) {
        try {
            artifact = stmt.executeStmt();
        } catch (Throwable throwable) {
            processException(throwable);
        }

        return artifact;
    }

    //==========================================================================
    private T processStmtClass(Class<T> stmtClass, NewObjectProcessor<T> newObjectProcessor) {
        try {
            artifact = stmtClass.getDeclaredConstructor().newInstance();

            newObjectProcessor.initialize(artifact);

        } catch (Throwable throwable) {
            processException(throwable);
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
        } catch (Throwable throwable) {
            this.processException(throwable);
        }

        return this;
    }

    //==========================================================================
    private void processException(Throwable throwable) {

        this.throwable = throwable;

        if (!silent) {
            throwable.printStackTrace();
            final String errMsg = getErrorMessage(throwable);
            //log.error("### {}: {}", throwable.getClass().getSimpleName(), errMsg);
            throw new InternalAppException(errMsg);
        } else {
            //log.warn("silent exception {}: {}", throwable.getClass().getSimpleName(), throwable.getLocalizedMessage());
        }
    }

    //==========================================================================
    public StmtProcessor processException(ExceptionProcessor exceptionProcessor) {

        if (StmtProcessor.notNull(this.throwable)) {
            exceptionProcessor.perform(this.throwable);
        }

        return this;
    }

    //==========================================================================
    public static final String getErrorMessage(Throwable throwable) {
        return InternalAppException.getExtendedErrMessage(throwable);
    }

    public static final String getStackTraceRaw(Throwable throwable) {
        return new StackTraceInfo(throwable).getStringStackTraceInfo();
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
    public static void sleep(int ms) {
        StmtProcessor.execute(() -> Thread.sleep(ms));
    }

    //=========================================================================
    public static final Boolean isNull(Object object) {
        return !StmtProcessor.notNull(object);
    }

    public static final Boolean notNull(Object object) {
        return (null != object);
    }

    public static final <T> T nvl(T object, T defObject) {
        return (null != object ? object : defObject);
    }    
    
    //==========================================================================
    public static <T> T assertNotNull(Class<?> clazz, Object object, String errMsg) {
//        Assert.notNull(object, String.format("%s: attribute|property|variable %s IS NULL! ", clazz.getSimpleName(), errMsg));
//        Assert.isTrue(object.hashCode() != 0, String.format("%s: hashcode of  '%s' is illegal! (%d)", clazz.getSimpleName(), errMsg, object.hashCode()));
        return (T) object;
    }

    public static void ifTrue(Boolean boolExp, VoidStmt stmt) {
        if (boolExp) {
            StmtProcessor.execute(stmt);
        }
    }

    public static void ifTrue(Boolean boolExp, VoidStmt stmtTrue, VoidStmt stmtFalse) {
        if (boolExp) {
            StmtProcessor.execute(stmtTrue);
        } else {
            StmtProcessor.execute(stmtFalse);
        }
    }

    public static void ifNotNull(Object object, VoidStmt stmt) {
        if (StmtProcessor.notNull(object)) {
            StmtProcessor.execute(stmt);
        }
    }

    public static <T> void ifNotNull(T t, VoidStmtP1<T> stmtP1) {
        if (StmtProcessor.notNull(t)) {
            StmtProcessor.execute(stmtP1, t);
        }
    }

    public static <T> void ifNotNull(T t, VoidStmtP1<T> stmtP1, VoidStmt stmtNull) {
        if (StmtProcessor.notNull(t)) {
            StmtProcessor.execute(stmtP1, t);
        } else {
            StmtProcessor.execute(stmtNull);
        }
    }

    public static void ifNotNull(Object object, VoidStmt stmtTrue, VoidStmt stmtFalse) {
        if (StmtProcessor.notNull(object)) {
            StmtProcessor.execute(stmtTrue);
        } else {
            StmtProcessor.execute(stmtFalse);
        }
    }

    public static void ifNull(Object object, VoidStmt stmt) {
        if (StmtProcessor.isNull(object)) {
            StmtProcessor.execute(stmt);
        }
    }

    public static void ifNull(Object object, VoidStmt stmtTrue, VoidStmt stmtFalse) {
        if (StmtProcessor.isNull(object)) {
            StmtProcessor.execute(stmtTrue);
        } else {
            StmtProcessor.execute(stmtFalse);
        }
    }

    public static <T> Collection<T> processCollection(Collection<T> collection, CollectionProcessor<T> collectionProcessor) {
        if (!collection.isEmpty()) {
            collectionProcessor.processCollection(collection);
        }

        return collection;
    }

    public static <T> Collection<T> fillCollection(Collection<T> collection, CollectionProcessor<T> collectionProcessor) {
        collectionProcessor.processCollection(collection);

        return collection;
    }

    public static void assertTrue(Boolean predicate, String errMsg) {
        //Assert.isTrue(predicate, String.format("Failed condition (%s)", errMsg));
    }

    public static void assertIsNull(Class<?> clazz, Object object, String errMsg) {
        //StmtProcessor.ifNotNull(object, notNullObj -> Assert.isNull(notNullObj, String.format("%s: attribute|property|variable %s IS NOT NULL! :%s ", clazz.getSimpleName(), errMsg, notNullObj.toString())));
    }

}
