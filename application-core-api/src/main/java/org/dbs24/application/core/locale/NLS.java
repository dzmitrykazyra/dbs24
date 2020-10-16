/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.locale;

//import org.dbs24.registry.api.ApplicationSetup;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.sysconst.SysConst;
import org.springframework.beans.factory.annotation.Value;

import java.time.*;
import java.time.format.DateTimeFormatter;

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
    public static final String getNlsParam(final String prmName, final String defVal) {

//        return ServiceLocator
//                .find(ApplicationSetup.class)
//                .getRegParam(prmName, defVal);
// временно
        return defVal;

    }

    //==========================================================================
    public static final String getStringDate(final LocalDate ld) {

        return (NullSafe.isNull(ld)) ? SysConst.EMPTY_STRING : ld.format(NLS.FORMAT_dd_MM_yyyy);

    }

    public static final String getStringTime(LocalTime lt) {

        return (NullSafe.isNull(lt)) ? SysConst.EMPTY_STRING : lt.format(NLS.FORMAT_HH_mm_ss);

    }

    public static final String getStringTimeMS(LocalTime lt) {

        return (NullSafe.isNull(lt)) ? SysConst.EMPTY_STRING : lt.format(NLS.FORMAT_HH_mm_ss_SSS);

    }

    //==========================================================================
    public static final String getStringDateTime(final LocalDateTime ldt) {

        return (NullSafe.isNull(ldt)) ? SysConst.EMPTY_STRING : ldt.format(NLS.FORMAT_dd_MM_yyyy__HH_mm_ss);

    }

    //==========================================================================
    public static final String getStringDateTimeMS(final LocalDateTime ldt) {

        return (NullSafe.isNull(ldt)) ? SysConst.EMPTY_STRING : ldt.format(NLS.FORMAT_dd_MM_yyyy__HH_mm_ss_SSS);

    }

    //==========================================================================
    public static final LocalDate long2LocalDate(final Long milliSeconds) {

        LocalDate result = null;

        if (NullSafe.notNull(milliSeconds)) {

            result = Instant.ofEpochMilli(milliSeconds)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        return (result);
    }

    //==========================================================================
    public static final String getObject2String(final Object value) {

        return NullSafe.create(value)
                //                .setResult((Object) SysConst.LONG_ZERO)
                .whenIsNull(() -> {
                    return SysConst.NOT_DEFINED;
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

    public static final LocalDate string2LocalDate(final String stringDate) {

        return LocalDate.parse(stringDate, NLS.DEFAULT_DATE_FORMATTER);

    }
}
