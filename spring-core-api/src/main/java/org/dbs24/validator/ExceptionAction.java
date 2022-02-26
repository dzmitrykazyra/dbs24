package org.dbs24.validator;

@FunctionalInterface
public interface ExceptionAction {
    void processException(Throwable throwable);
}
