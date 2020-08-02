/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.exception.api;

import com.kdg.fs24.application.core.sysconst.SysConst;

/**
 *
 * @author kazyra_d
 */
public class InternalAppException extends RuntimeException {

    public InternalAppException() {
        super();

    }

    public InternalAppException(final String aErrMsg) {
        super(aErrMsg);
    }

    //==========================================================================
    public String getExtendedErrMessage() {

        return getExtendedErrMessage(this);
    }

    //==========================================================================
    public static String getExtendedErrMessage(final Throwable th) {
        String errMsg = "no error details";
        String errCause = SysConst.EMPTY_STRING;

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
    public static String getExtendedErrMessage(final Throwable th, final String procedure) {

        String errMsg = String.format("Exception in procedure '%s': ", procedure, getExtendedErrMessage(th));

        return errMsg;
    }

}
