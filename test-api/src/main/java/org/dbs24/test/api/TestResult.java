/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test.api;

import static org.dbs24.consts.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
public class TestResult {

    private String logMsg;
    private Integer errCount;
    private String errKeyWord;

    public TestResult() {
        super();
    }

    //==========================================================================
    public String getLogMsg() {
        return logMsg;
    }

    public void addLogMsg( String logMsg) {
        //this.logMsg = (String) NullSafe.nvl(this.logMsg, "");
        if (null == this.logMsg) {
            this.logMsg = EMPTY_STRING;
        }
        this.logMsg = this.logMsg.concat(logMsg);
    }

    public Integer getErrCount() {
        return errCount;
    }

    public void setErrCount( Integer errCount) {
        this.errCount = errCount;
    }

    public String getErrKeyWord() {
        return errKeyWord;
    }

    public void setErrKeyWord( String errKeyWord) {
        this.errKeyWord = errKeyWord;
    }

}
