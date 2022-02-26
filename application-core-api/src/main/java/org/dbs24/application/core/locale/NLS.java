/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.locale;

//import org.dbs24.registry.api.ApplicationSetup;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.SysConst.*;
import java.util.concurrent.TimeUnit;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class NLS extends ObjectRoot {

    //public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_FORMAT = NLS.getNlsParam("DATE_FORMAT", "dd.MM.yyyy");
    public static final String DATETIME_MS_FORMAT = NLS.getNlsParam("DATETIME_MS_FORMAT", "dd.MM.yyyy HH:mm:ss.SSS");
    public static final String DATETIME_FORMAT = NLS.getNlsParam("DATETIME_FORMAT", "dd.MM.yyyy HH:mm:ss");
    public static final String TIME_FORMAT = NLS.getNlsParam("TIME_FORMAT", "HH:mm:ss");
    public static final String TIME_FORMAT_MS = NLS.getNlsParam("TIME_FORMAT_MS", "HH:mm:ss.SSS");
    public static final String APP_LOCALE = NLS.getNlsParam("APP_LOCALE", "ru");

    public static final DateTimeFormatter FORMAT_dd_MM_yyyy__HH_mm_ss_SSS = DateTimeFormatter.ofPattern(DATETIME_MS_FORMAT);
    public static final DateTimeFormatter FORMAT_dd_MM_yyyy__HH_mm_ss = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    public static final DateTimeFormatter FORMAT_dd_MM_yyyy = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter FORMAT_HH_mm_ss = DateTimeFormatter.ofPattern(TIME_FORMAT);
    public static final DateTimeFormatter FORMAT_HH_mm_ss_SSS = DateTimeFormatter.ofPattern(TIME_FORMAT_MS);

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = FORMAT_dd_MM_yyyy;

    //==========================================================================
//    static {
//        LogService.LogInfo(NLS.class, () -> String.format("NEW_DATE_FORMAT = %s", NLS.NEW_DATE_FORMAT));
//    }
    public static final String getNlsParam(String prmName, String defVal) {

//        return ServiceLocator
//                .find(ApplicationSetup.class)
//                .getRegParam(prmName, defVal);
// временно
        return defVal;

    }

    //==========================================================================
    public static final String getStringDate(LocalDate ld) {

        return (NullSafe.isNull(ld)) ? EMPTY_STRING : ld.format(NLS.FORMAT_dd_MM_yyyy);

    }

    public static final String getStringTime(LocalTime lt) {

        return (NullSafe.isNull(lt)) ? EMPTY_STRING : lt.format(NLS.FORMAT_HH_mm_ss);

    }

    public static final String getStringTimeMS(LocalTime lt) {

        return (NullSafe.isNull(lt)) ? EMPTY_STRING : lt.format(NLS.FORMAT_HH_mm_ss_SSS);

    }

    //==========================================================================
    public static final String getStringDateTime(LocalDateTime ldt) {

        return (NullSafe.isNull(ldt)) ? EMPTY_STRING : ldt.format(NLS.FORMAT_dd_MM_yyyy__HH_mm_ss);

    }

    //==========================================================================
    public static final String getStringDateTimeMS(LocalDateTime ldt) {

        return (NullSafe.isNull(ldt)) ? EMPTY_STRING : ldt.format(NLS.FORMAT_dd_MM_yyyy__HH_mm_ss_SSS);

    }

    //==========================================================================
    public static final LocalDate long2LocalDate(Long milliSeconds) {

        LocalDate result = null;

        if (NullSafe.notNull(milliSeconds)) {

            result = Instant.ofEpochMilli(milliSeconds)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        return (result);
    }

    //==========================================================================    
    public static final String localDateTime2String(LocalDateTime ldt) {
        return ldt.format(NLS.FORMAT_dd_MM_yyyy__HH_mm_ss);
    }

    //==========================================================================
    public static final LocalDateTime long2LocalDateTime(Long milliSeconds) {

        LocalDateTime result = null;

        if (StmtProcessor.notNull(milliSeconds)) {

            result = Instant.ofEpochMilli(milliSeconds)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }

        return (result);
    }

    //==========================================================================
    public static final Long localDateTime2long(LocalDateTime ldt) {

        Long result = null;

        if (StmtProcessor.notNull(ldt)) {

            ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
            result = zdt.toInstant().toEpochMilli();
        }

        return (result);
    }

    //==========================================================================
    public static final String getObject2String(Object value) {

        return NullSafe.create(value)
                //                .setResult((Object) LONG_ZERO)
                .whenIsNull(() -> {
                    return NOT_DEFINED;
                })
                .safeExecute2result((ns_value) -> {
                    final String stringValue;
                    final Class<?> valueClass = ns_value.getClass();

                    if (valueClass.equals(LocalDate.class)) {
                        stringValue = NLS.getStringDate((LocalDate) ns_value);
                    } else {
                        if (valueClass.equals(LocalDateTime.class)) {
                            stringValue = NLS.getStringDateTime((LocalDateTime) ns_value);
                        } else {
                            stringValue = String.format("%s", ns_value);
                        }
                    }

                    return stringValue;
                })
                .<String>getObject();
    }
    //==========================================================================

    public static final LocalDate string2LocalDate(String stringDate) {

        return LocalDate.parse(stringDate, NLS.DEFAULT_DATE_FORMATTER);

    }

    //==========================================================================    
    public static final LocalDateTime maxLdt(LocalDateTime d1, LocalDateTime d2) {
        return d1.compareTo(d2) > 0 ? d1 : d2;
    }

    public static final LocalDateTime minLdt(LocalDateTime d1, LocalDateTime d2) {
        return d1.compareTo(d2) < 0 ? d1 : d2;
    }

    //==========================================================================
    public static final String d1d2Diff(Long millis) {

        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        final StringBuilder sb = new StringBuilder(64);

        Boolean needAppend = (days > 0);

        if (needAppend) {
            sb.append(days);
            sb.append(" day(s) ");
        }

        needAppend = needAppend || (hours > 0);

        if (needAppend) {
            sb.append(hours);
            sb.append(" hour(s) ");
        }

        needAppend = needAppend || (minutes > 0);

        if (needAppend) {
            sb.append(minutes);
            sb.append(" minute(s) ");
        }

        needAppend = needAppend || (seconds > 0);

        if (needAppend) {
            sb.append(seconds);
            sb.append(" second(s) ");
        }

        return sb.toString();

    }

    //==========================================================================
    public static final String d1d2Diff(LocalDateTime d1, LocalDateTime d2) {

        return d1d2Diff(ChronoUnit.MILLIS.between(d1, d2));

    }
}
