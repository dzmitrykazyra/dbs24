/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.stmt.StmtProcessor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Long.valueOf;
import static org.dbs24.consts.SysConst.*;

public final class StringFuncs {

    public static final Locale locale = new Locale(APP_LOCALE);

    public static Boolean isNumericValue(String numValue) {
        return numValue.matches("\\d+");
    }

    public static Boolean isBigDecimalValue(String numValue) {

        Boolean result = Boolean.FALSE;

        try {
            new BigDecimal(numValue);
            result = Boolean.TRUE;
        } catch (Throwable t) {
        }
        return result;
    }

    public static Integer getIntegerValue(String intValue, String valueName) {

        StmtProcessor.assertNotNull(String.class, intValue, valueName);

        return Optional.ofNullable(intValue.matches("\\d+") ? Integer.valueOf(intValue) : null)
                .orElseThrow(() -> new java.lang.NumberFormatException(String.format("Bad integer value (%s='%s')", valueName, intValue).toUpperCase()));

    }

    public static String long2StringFormatter(Long value) {
        return NumberFormat.getInstance(locale).format(value);
    }

    public static String long2StringFormatter(Integer value) {
        return NumberFormat.getInstance(locale).format(value);
    }

    //==========================================================================
    private static final <T> T getDefaultNumericValue() {

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
    public static <T> T getSafeDate(String value) {

        return (T) NullSafe.create(value)
                //                .setResult((Object) LONG_ZERO)
                .execute2result(() -> {

                    return LocalDate.parse(value, DEFAULT_DATE_FORMATTER);
                })
                .<T>getObject();

    }

    //==========================================================================
    public static String truncString(String value, Integer limit) {

        return StmtProcessor.notNull(value) ? value.isEmpty() ? value : value.substring(0, Math.min(value.length(), limit) - 1) : STRING_NULL;

    }

    //==========================================================================
    public static String createRandomString(int limit) {

        return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("-", "").substring(0, limit).toUpperCase();
    }

    public static Long stringVersion2long(String version) {

        final String[] recs = version.replaceAll(" ", "").split("\\.");
        var longVersion = valueOf(0);

        for (int i = recs.length - 1; i >= 0; --i) {
            longVersion = longVersion + valueOf(recs[i]) * (long) Math.pow(10, 3 * (recs.length - i - 1));
        }

        return longVersion;
    }
}
