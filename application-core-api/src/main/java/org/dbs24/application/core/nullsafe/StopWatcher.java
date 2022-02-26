/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.nullsafe;

/**
 *
 * @author kazyra_d
 */
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.dbs24.application.core.locale.NLS;

public final class StopWatcher {

    private String unitName;
    private final LocalDateTime startDateTime;

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }    
    
    public String getStringStartDateTime() {
        return NLS.getStringDateTime(startDateTime);
    }
    
    public String getUnitName() {
        return this.unitName;
    }

    public StopWatcher(String unitName) {
        this();
        this.unitName = unitName;
    }

    public StopWatcher() {
        this.startDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        this.unitName = this.getRunnigProcName(5);
    }

    public static StopWatcher create() {
        return new StopWatcher();
    }

    public static StopWatcher create(String unitName) {
        return new StopWatcher(unitName);
    }

    public static StopWatcher createWhen(Boolean createCondition, String unitName) {
        return createCondition ? new StopWatcher(unitName) : null;
    }

    private String getRunnigProcName(int index) {
        final StackTraceElement ste = Thread.currentThread()
                .getStackTrace()[index];
        return String.format("%s.%s [%s]",
                ste.getClassName(),
                ste.getMethodName(),
                ste.getFileName());
    }

    public long getExecutionTime() {
        return ChronoUnit.MILLIS.between(startDateTime, LocalDateTime.now());
    }
    //==========================================================================

    final int msBorder = 10000;
    final int secBorder = 300000;
    final int hourBorder = 3600000;

    public String getStringExecutionTime() {

        return getStringExecutionTime("finished in");

    }

    //==========================================================================
    public String getStringExecutionTime(String info) {

        final long ms = this.getExecutionTime();
        final String result;

        if (ms < msBorder) {

            final long value = (ms < msBorder) ? ms : (ms < secBorder) ? (ms / 1000) : ((ms < hourBorder) ? (ms / 60000) : (ms / 3600000));
            final String str = (ms < msBorder) ? "ms" : (ms < secBorder) ? "Seconds" : ((ms < hourBorder) ? "Minute(s)" : "hour(s)");

            result = String.format("%s: %s %d %s",
                    unitName,
                    info,
                    value, str);

        } else {

            result = String.format("%s: %s", info, NLS.d1d2Diff(ms));
        }

        return result;
    }
}
