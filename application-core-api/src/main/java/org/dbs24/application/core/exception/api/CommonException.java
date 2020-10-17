/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.exception.api;

import static org.dbs24.application.core.sysconst.SysConst.*;

import java.sql.SQLException;

/**
 *
 * @author kazyra_d
 */
class ExternalException extends InternalAppException {
}

public class CommonException extends InternalAppException {

    static final String CRLF = "\r\n";
    static final String TAB = "\t";

    // общая процедура обработки исключений
    public static void ProcessException(Exception e) throws InternalAppException {
        CommonException.ProcessException(e, EMPTY_STRING, EMPTY_STRING);
    }

    // общая процедура обработки исключений
    public static void ProcessException(Exception e, final String errMsg) throws InternalAppException {
        CommonException.ProcessException(e, EMPTY_STRING, errMsg);
    }

    // общая процедура обработки исключений
    public static void ProcessException(final SQLException e, final String errMsg) throws InternalAppException {
        CommonException.ProcessException(e, EMPTY_STRING, errMsg);
    }

    // общая процедура обработки исключений
    public static void ProcessException(Exception e, final String errMsg, final String procedureName) throws InternalAppException {
        String DetailErrMsg = "Error during program execution!" + CRLF;

        DetailErrMsg = DetailErrMsg + CRLF
                + TAB + String.format("Exception Class: '%s'", e.getClass().getName()) + ";" + CRLF;

        if (errMsg.length() > 0) {
            DetailErrMsg = DetailErrMsg + CRLF
                    + TAB + "UserMsg: " + '"' + errMsg + '"';

            if (procedureName.length() > 0) {
                DetailErrMsg = DetailErrMsg + CRLF
                        + TAB + "Procedure: " + '"' + procedureName + '"' + ";";
            }
        }

        DetailErrMsg = DetailErrMsg + CRLF
                + TAB + "ExcMsg: " + InternalAppException.getExtendedErrMessage(e) + ";" + CRLF;

        System.out.println("ProcessException\n//==================================" + CRLF + TAB + DetailErrMsg);

        throw new InternalAppException(InternalAppException.getExtendedErrMessage(e));

    }

}
