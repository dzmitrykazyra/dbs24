/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.nullsafe;

import java.util.Arrays;

/**
 *
 * @author kazyra_d
 */
public final class StackTraceInfo {

    private String restRecord = " StackTrace details: \n";
    private final Throwable throwable;
    private final String shift = "############";

    public StackTraceInfo(Throwable th) {
        this.throwable = th;

        this.addTraceRecord(String.format("%s %s: '%s'\n",
                this.shift,
                th.getClass().getCanonicalName(),
                th.getMessage()));

    }

    public void addTraceRecord(String recInfo) {
        restRecord = restRecord.concat(recInfo);
    }

    private void addTraceElement(StackTraceElement ste) {
        restRecord = restRecord.concat(String.format("%s %s.%s() (%d)\n",
                this.shift,
                ste.getClassName(),
                ste.getMethodName(),
                ste.getLineNumber()));
    }

    public String getPrintRecord() {
        return restRecord;
    }

    public String getStringStackTraceInfo() {

        Arrays.stream(throwable.getStackTrace())
                //                .sorted((st1, st2) -> {
                //                    return (Integer.valueOf(st1.getLineNumber()).compareTo(st2.getLineNumber()));
                //                })
                .limit(50)
                .forEach((ste) -> {
                    this.addTraceElement(ste);
                });

        return this.getPrintRecord();

    }

}
