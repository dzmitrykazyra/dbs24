/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.nullsafe;

import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.log.LogService;
import static org.dbs24.application.core.sysconst.SysConst.*;

import java.math.BigDecimal;
import java.time.LocalDate;

//import org.dbs24.test.api.TestConst;
/**
 *
 * @author Козыро Дмитрий
 */
public class NullSafe { // implements NullSafe {

    protected final Object monitorLock = new Object();
    public static final Boolean EXCEPTION_FLAG = Boolean.TRUE;
    public static final Boolean DONT_THROW_EXCEPTION = Boolean.TRUE;
    public static final Boolean THROW_EXCEPTION = Boolean.FALSE;
    public static final Boolean LOG_EXCEPTION = Boolean.FALSE;
    private volatile StopWatcher stopWatcher;

    private String blockName;
    private String exceptionMsg;
    private volatile Object result;
    private volatile Throwable throwable;
    private Boolean logException = BOOLEAN_TRUE; // признак необходимости записи в лог возникшего исключения
    private Boolean exceptionFlag = Boolean.FALSE;
    protected volatile Object inputParam;
    private volatile CodeBlockResult cb4watcher;

    //public static final Collection<ExceptionEvent> EXCEPTION_EVENTS = new ArrayList();
    //--------------------------------------------------------------------------
    public static final NullSafe create() {
        return new NullSafe();
    }

    //--------------------------------------------------------------------------
    public static final NullSafe create(final Object object) {
        return new NullSafe(object);
    }

    //--------------------------------------------------------------------------
    public static final NullSafe create(final Object inputParam, final Boolean silentException) {
        return new NullSafe(inputParam, silentException);
    }

    //--------------------------------------------------------------------------
    public NullSafe() {
        super();
        this.setResult(OBJECT_NULL);
        //stopWatcher = StopWatcher.create();
    }

    public NullSafe(final Object object) {
        this();
        this.setResult(object);
        if (null != object) {
//            this.setBlockName(String.format("%s (%s)",
//                    object.toString(),
//                    object.getClass().getSimpleName()));
            this.setBlockName(String.format("%s",
                    object.getClass().getSimpleName()));
        }
    }

    //==========================================================================
    public NullSafe(final Object inputParam, final Boolean silentException) {
        this(inputParam);
        this.inputParam = inputParam;
        this.setResult(inputParam);
        if (null != inputParam) {
            this.setBlockName(String.format("%s", inputParam.getClass().getCanonicalName()));
        }
        this.setLogException(!silentException);
    }

    //==========================================================================
//    public static Object nvl(Object value, final Object defValue) {
//        return (Object) ((NullSafe.notNull(value)) ? value : defValue);
//    }
//    public static Integer nvl(final Integer value, final Integer defValue) {
//        return ((NullSafe.notNull(value)) ? value : defValue);
//    }
//    //==========================================================================
//
//    public static String nvl(String value, final String defValue) {
//        return ((NullSafe.notNull(value)) ? value : defValue);
//    }
    //==========================================================================
    public NullSafe inititialize(final CodeBlock codeBlock) {
        return this.execute(codeBlock);
    }

    //==========================================================================
    public NullSafe execute(final CodeBlock codeBlock) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            codeBlock.someCode();
        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    //==========================================================================
    public NullSafe execute(final CodeBlockParam codeBlockParam) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            codeBlockParam.someCode(this.inputParam);
        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    //==========================================================================
    public NullSafe safeExecute(final CodeBlock codeBlock) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            if (null != this.getObject()) {
                codeBlock.someCode();
            }
        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    public NullSafe safeExecute(final CodeBlockParam codeBlockParam) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            if (null != this.getObject()) {
                codeBlockParam.someCode(this.getObject());
            }
        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    public NullSafe safeExecute(final CodeBlockParam codeBlockParam, final Object defaultValue) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);

            if (null != this.getObject()) {
                codeBlockParam.someCode(this.getObject());
            } else {
                this.setResult(defaultValue);
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    public NullSafe safeExecute(final CodeBlockParam2Result codeBlockParam2Result, final Object defaultValue) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);

            if (null != this.getObject()) {
                this.setResult(codeBlockParam2Result.someCode(this.getObject()));
            } else {
                this.setResult(defaultValue);
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    //==========================================================================
    public NullSafe execute2result(final CodeBlockResult codeBlockResult) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            this.setResult(codeBlockResult.someCodeResult());

        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    //==========================================================================
    public NullSafe execute2result(final CodeBlockResult codeBlockResult, final Object defaultValue) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            this.setResult(codeBlockResult.someCodeResult());
        } catch (Throwable th) {
            this.processThrowableException(th);
            this.setResult(defaultValue);
        } finally {
            printWatcher();
        }

        return this;
    }

    //==========================================================================
    public NullSafe execute2result(final CodeBlockParam2Result codeBlockParam2Result) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            this.setResult(codeBlockParam2Result.someCode(this.inputParam));
        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    //==========================================================================
    public NullSafe safeExecuteWhileNotNull(final CodeBlockParam2Result codeBlockParam2Result) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            while ((null != this.getObject())) {
                this.setResult(codeBlockParam2Result.someCode(this.getObject()));
            }
        } catch (Throwable th) {
            this.processThrowableException(th);
        }
        return this;
    }

    //==========================================================================
    @Deprecated
    public NullSafe safeExecute2result(final CodeBlockParam2Result codeBlockParam2Result) {
        try {

            if (null != this.getObject()) {
                this.setResult(codeBlockParam2Result.someCode(this.getObject()));
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        }
        return this;
    }

    //==========================================================================
    public NullSafe safeExecute2result(final CodeBlockResult codeBlockResult) {
        try {
            if (null != codeBlockResult) {
                if (null != this.getObject()) {
                    this.setResult(codeBlockResult.someCodeResult());
                }
            }

        } catch (final Throwable th) {
            this.processThrowableException(th);
        }
        return this;
    }

    //==========================================================================
    public NullSafe catchException(final CodeBlockEx codeBlockEx) {
        if (this.getExceptionFlag() && this.getLogException()) {
            if (null != this.getThrowable()) {
                //LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), () -> "StackTraceInfo:");
                processThrowableException(this.getThrowable());
            }
            return this.processException(codeBlockEx);
        }
        return this;
    }

    //==========================================================================
    private NullSafe processException(final CodeBlockEx codeBlockEx) {
        try {
            //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this), blockName);
            codeBlockEx.someCode(this.getThrowable());
        } catch (Throwable th) {
            this.processThrowableException(th);
        }
        return this;
    }

    //===========================================================================    
    public NullSafe whenIsNull(final CodeBlockResult codeBlockResult) {
        try {

            if (null == this.getObject()) {
                this.setResult(codeBlockResult.someCodeResult());
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        }

        return this;
    }

    public NullSafe whenIsNull(final CodeBlock codeBlock) {
        try {

            if (null == this.getObject()) {
                codeBlock.someCode();
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        }

        return this;
    }

    //===========================================================================    
    public NullSafe whenIsNull_Safe(final CodeBlockResult codeBlockResult) {
        try {
            if (null == this.getObject()) {
                synchronized (this.monitorLock) {

                    if (null == this.getObject()) {
                        this.setResult(codeBlockResult.someCodeResult());
                    }
                }
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        }

        return this;
    }

    public NullSafe whenIsNull_Safe(final CodeBlock codeBlock) {
        try {

            if (null == this.getObject()) {
                synchronized (this.monitorLock) {

                    if (null == this.getObject()) {
                        codeBlock.someCode();
                    }
                }
            }

        } catch (Throwable th) {
            this.processThrowableException(th);
        }

        return this;
    }

    //==========================================================================
    public NullSafe finallyBlock(final CodeBlock codeBlock) {
        return this.execute(codeBlock);
    }

    //==========================================================================
    public NullSafe registerOuterException(final Throwable th, final String thMsg) {
        this.addException(th, thMsg);
        return this;
    }

    //==========================================================================
    public NullSafe registerOuterException(final Throwable th) {
        if (this.getExceptionFlag()) {
            this.addException(th, th.getMessage().concat(String.format("\n '%s'", this.exceptionMsg)));
        }
        return this;
    }

    //==========================================================================
    protected final void processThrowableException(final Throwable th) {

        final String resultClass = String.format("result class: '%s' ", null != result ? result.getClass().getCanonicalName() : NOT_DEFINED);
        final String resultValue = String.format("result value: '%s' ", null != result ? NullSafe.getStringObjValue(this.result) : NOT_DEFINED);

        final String inputParamClass = String.format("inputParam class: '%s' ", null != inputParam ? inputParam.getClass().getCanonicalName() : NOT_DEFINED);
        final String inputParamValue = String.format("inputParam value: '%s' ", null != inputParam ? NullSafe.getStringObjValue(this.inputParam) : NOT_DEFINED);

        final String thMsg = String.format("%s \n %s \n %s \n %s \n %s \n %s",
                InternalAppException.getExtendedErrMessage(th),
                th.getMessage(),
                resultClass,
                resultValue,
                inputParamClass,
                inputParamValue);

        this.setThrowable(th);

        this.setExceptionFlag(EXCEPTION_FLAG);
        this.setExceptionMsg(thMsg);

        if (this.getLogException()) {

//            LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this),
//                    () -> String.format("%s: '%s' ",
//                            this.getBlockName(),
//                            this.getExceptionMsg()));
            this.addException(th, thMsg);
        }
    }

    //==========================================================================
    public NullSafe catchMsgException(final CodeBlockExMsg codeBlockExMsg) {
        if (this.getExceptionFlag()) {
            codeBlockExMsg.someCode(this.getExceptionMsg());
            if (null != this.getThrowable()) {
//                LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), () -> "StackTraceInfo:");

                processThrowableException(this.getThrowable());
            }
        }
        return this;
    }

    //==========================================================================
    public NullSafe catchException2result(final CodeBlockEx2Result codeBlockEx2Result) {
        if (this.getExceptionFlag()) {
            try {
                this.setResult(codeBlockEx2Result.someCode(this.getThrowable()));
                if (null != this.getThrowable()) {
//                    LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), () -> "StackTraceInfo:");
                    processThrowableException(this.getThrowable());
                }
            } catch (Throwable th) {
                this.processThrowableException(th);
            }
        }
        return this;
    }

    //==========================================================================
    private void addException(final Throwable th, final String thMsg) {

//        final Thread currThread = Thread.currentThread();
        //запуск в отдельном потоке
        new Thread(() -> {
//            synchronized (NullSafe.EXCEPTION_EVENTS) {
//                //final LocalDateTime expTime = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
////                final List<ExceptionEvent> ev_List = (List<ExceptionEvent>) NullSafe.exceptionEvents
////                        .stream()
////                        .filter(ev -> ev.getThrowable().getClass().equals(th.getClass())
////                        && ev.getExcDateTime().isAfter(expTime))
////                        .collect(Collectors.toList());
//
//                // зарегистрировали исключение
//                NullSafe.registerInternalException(th, thMsg);
//
//                // печатаем стектрейс, если такого исключения не было последние ХХ минут
//                //  if (ev_List.isEmpty()) {
            NullSafe.internalPrintStackTrace(th);
//                // }
//            }
        }).start();
    }

    //==========================================================================
    private static void registerInternalException(final Throwable th, final String thMsg) {

//        LogService.LogErr(th.getClass(), () -> thMsg);
//
//        synchronized (NullSafe.EXCEPTION_EVENTS) {
//
//            NullSafe.EXCEPTION_EVENTS.add(new ExceptionEvent()
//                    .setThrowable(th)
//                    .setExcDateTime(LocalDateTime.now())
//                    .setExcMessage(thMsg));
//        }
    }

    //======================================================================
    public static void printStackTrace(final Throwable th) {
        NullSafe.internalPrintStackTrace(th);

    }

    private static void internalPrintStackTrace(final Throwable th) {

        final String callStack = NullSafe.getStackTraceRaw(th);

        LogService.LogErr(th.getClass(), () -> callStack);
//
//        NullSafe.EXCEPTION_EVENTS.add(new ExceptionEvent()
//                .setThrowable(th)
//                .setExcDateTime(LocalDateTime.now())
//                .setExcMessage(callStack));
    }

    //==========================================================================
    public static final String getStackTraceRaw(final Throwable th) {
        return new StackTraceInfo(th).getStringStackTraceInfo();
    }

    //==========================================================================
    private final static <T> T getDefaultNumericValue() {

        T t;

        try {
            t = (T) LONG_ZERO;
        } catch (Throwable th) {
            try {
                t = (T) INTEGER_ZERO;
            } catch (Throwable th1) {
                t = (T) BigDecimal.ZERO;
            }
        }
        return t;
    }

    //==========================================================================
    public static <T> T getSafeNumeric(final String value) {

        return (T) NullSafe.create(value, NullSafe.DONT_THROW_EXCEPTION)
                .setResult((Object) (T) NullSafe.<T>getDefaultNumericValue())
                .execute2result(() -> {

                    if ((value.indexOf('.') + value.indexOf(',')) > 0) {
                        return new BigDecimal(value);
                    } else {
                        return Long.parseLong(value);
                    }
                }).<T>getObject();

    }

    //==========================================================================
    public static <T> T getSafeDate(final String value) {

        return (T) NullSafe.create(value)
                //                .setResult((Object) LONG_ZERO)
                .execute2result(() -> {

                    return LocalDate.parse(value, DEFAULT_DATE_FORMATTER);
                })
                .<T>getObject();

    }

////////////////////////////////////////////////////////////////////////////
    public NullSafe setResult(final Object object) {
        this.result = null;
        this.result = object;
        return this;
    }

//    
//    @Deprecated
//    public Object getObject() {
//        return this.result;
//    }
    public <V> V getObject() {

        V nsResult = (V) OBJECT_NULL;

        try {
            nsResult = (V) this.result;
        } catch (Throwable th) {
            this.processThrowableException(th);
            //nsResult = (V) OBJECT_NULL;
        } finally {
            printWatcher();
        }

        return nsResult;
    }

    public String getExceptionMsg() {
        return this.exceptionMsg;
    }

    public void setExceptionMsg(final String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(final String blockName) {
        this.blockName = blockName;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }

    public Boolean getLogException() {
        return logException;
    }

    public void setLogException(final Boolean logException) {
        this.logException = logException;
    }

    public Boolean getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(final Boolean exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    //==========================================================================
    // вывести исключение наружу, если оно возникло
    public NullSafe throwException() throws RuntimeException {

        if ((null != this.getThrowable()) && this.getLogException()) {

            LogService.LogErr(this.getThrowable().getClass(),
                    () -> String.format("%s: %s",
                            this.getThrowable().getClass().getSimpleName(),
                            this.getThrowable().getMessage()));
            throw new RuntimeException(this.getThrowable());
        }
        return this;
    }

    //==========================================================================
    public <V> NullSafe showResult(final String maskFormat) {

//        try {
//
//            final String msgKey;
//
//            if (null != getStopWatcher()) {
//                msgKey = getStopWatcher().getUnitName();
//            } else {
//                msgKey = LogService.getCurrentObjProcName(this);
//            }
//
//            LogService.LogInfo(this.getClass(), () -> String.format("%s: '%s'", msgKey,
//                    String.format(maskFormat, this.<V>getObject())));
//        } catch (Throwable th) {
//            this.processThrowableException(th);
//        }
        return this;
    }

    //==========================================================================
    public NullSafe logWatcher() {
        //LogService.LogInfo(this.getClass(), stopWatcher.getTimeExecStringMillis());
        return this;
    }

    //==========================================================================
    private void printWatcher() {

        if (null != getStopWatcher()) {
            //LogService.LogInfo(this.getClass(), stopWatcher.getTimeExecStringMillis());

            synchronized (StopWatcher.class) {

                if (null != getStopWatcher()) {

                    final String details = NullSafe.create(this.cb4watcher)
                            .whenIsNull(() -> EMPTY_STRING)
                            .safeExecute2result(this.cb4watcher)
                            .<String>getObject();

                    final String watchString = String.format("%s: %s",
                            getStopWatcher().getStringExecutionTime(),
                            details);

//                    LogService.LogInfo(this.getClass(), () -> watchString);
                    stopWatcher = null;
                }
            }
        }
    }

    public NullSafe initWatcher() {
        //LogService.LogInfo(this.getClass(), stopWatcher.getTimeExecStringMillis());
//        if (TestConst.TEST_MODE_RUNNING) {
//            final StopWatcher stopWatcher = StopWatcher.create();
//            this.stopWatcher = stopWatcher;
//        }
        return this;
    }

    public NullSafe initWatcher(final String watcherName) {
        //LogService.LogInfo(this.getClass(), stopWatcher.getTimeExecStringMillis());
//        if (TestConst.PRINT_SQL) {
//            final StopWatcher stopWatcher = StopWatcher.create(watcherName);
//            this.stopWatcher = stopWatcher;
//        }
        return this;
    }

    public NullSafe initWatcher(final String watcherName, final CodeBlockResult cb4watcher) {

        this.initWatcher(watcherName);
        this.cb4watcher = cb4watcher;

        return this;
    }

    @Deprecated
    public <T> NullSafe createObject(final CodeBlockResult codeBlockResult) {

        try {
            final T object = (T) codeBlockResult.someCodeResult();
            this.setResult(object);
        } catch (Throwable th) {
            this.processThrowableException(th);
        } finally {
            printWatcher();
        }
        return this;
    }

    public static <T> T createObject(final Class<T> clazz) {
        return NullSafe.create()
                .execute2result(() -> clazz.newInstance())
                .throwException()
                .<T>getObject();
    }

    public static <T> T createObject(final Class<T> clazz, final ObjectBuilder<T> objectBuilder) {
        return NullSafe.create()
                .execute2result(() -> {
                    final T newObject = NullSafe.createObject(clazz);
                    objectBuilder.processNewObject(newObject);
                    return newObject;
                })
                .throwException()
                .<T>getObject();
    }

    public static <T, V> T createObject(
            final Class<T> clazz,
            final V constructorArg) {
        return NullSafe.<T, V>createObject(clazz, constructorArg, null);
    }

    public static <T, V> T createObject(
            final Class<T> clazz,
            final V constructorArg,
            final ObjectBuilder objectBuilder) {
        return NullSafe.create()
                .execute2result(() -> {
                    final Class[] cArgList = new Class[1];
                    cArgList[0] = constructorArg.getClass(); //First argument is of *object* type Long
                    final T newObject = clazz.getDeclaredConstructor(cArgList).newInstance(constructorArg);
                    if (NullSafe.notNull(objectBuilder)) {
                        objectBuilder.processNewObject(newObject);
                    }
                    return newObject;
                })
                .throwException()
                .<T>getObject();
    }

    public static Thread runNewThread(
            final Runnable target) {
        final Thread thread = new Thread(target);
        thread.start();
        return thread;
    }

    public StopWatcher getStopWatcher() {
        return stopWatcher;
    }
    //==========================================================================    

    public static String getStringObjValue(final Object object) {

        return NullSafe.create(STRING_NULL, NullSafe.DONT_THROW_EXCEPTION)
                .whenIsNull(() -> ((LocalDate) object).format(FORMAT_dd_MM_yyyy))
                .whenIsNull(() -> String.format("%s", object))
                .whenIsNull(() -> String.format("%d", object))
                .whenIsNull(() -> String.format("%f", object))
                .whenIsNull(() -> String.format("%b", object))
                .whenIsNull(() -> NOT_DEFINED)
                .<String>getObject();
    }

    //==========================================================================
    public static final Boolean isNull(final Object object) {
        return !NullSafe.notNull(object);
    }

    public static final Boolean notNull(final Object object) {
        return (null != object);
    }

    public static final <V> V nvl(final V value, final V defValue) {
        return (V) ((NullSafe.notNull(value)) ? value : defValue);
    }

    public static final String getErrorMessage(Throwable th) {
        return InternalAppException.getExtendedErrMessage(th);
    }
}
