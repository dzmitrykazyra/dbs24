/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.exception.api;

import static org.dbs24.consts.SysConst.*;

/**
 *
 * @author kazyra_d
 */
public class InternalAppException extends RuntimeException {

    public InternalAppException() {
        super();

    }

    public InternalAppException( String aErrMsg) {
        super(aErrMsg);
    }

    //==========================================================================
    public String getExtendedErrMessage() {

        return getExtendedErrMessage(this);
    }

    //==========================================================================
    public static String getExtendedErrMessage( Throwable th) {
        String errMsg = "no error details";
        String errCause = EMPTY_STRING;

        if (null != th) {

            if (null != th.getCause()) {
                errCause = String.format("\n(%s: %s)", th.getCause().getClass().getName(), th.getCause().getMessage());
            }

            errMsg = String.format("%s: %s%s",
                    th.getClass().getName(), th.getMessage(), errCause);

        }

        return errMsg;
    }

    //==========================================================================
    public static String getExtendedErrMessage( Throwable th, String procedure) {

        String errMsg = String.format("Exception in procedure '%s': ", procedure, getExtendedErrMessage(th));

        return errMsg;
    }

}
