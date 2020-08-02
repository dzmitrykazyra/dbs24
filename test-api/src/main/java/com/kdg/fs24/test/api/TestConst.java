/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.test.api;

/**
 *
 * @author Козыро Дмитрий
 */
import com.kdg.fs24.application.core.sysconst.SysConst;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class TestConst {

    public static volatile Boolean TEST_MODE_RUNNING = Boolean.FALSE;
    public static volatile Boolean PRINT_SQL = Boolean.FALSE;

    public static final String DELIMITER = "++++++++++++++++ ";
    public static final Boolean DEV_MODE = Boolean.TRUE;
    public static final Boolean BUILD_MODE = Boolean.FALSE;

    //==========================================================================
    public static final String TEST_PACKAGE_NAME = "com.kdg.fs24.tests";
    public static final int TEST_ISSUE_LOAN = 100001;
    public static final int TEST_4CASHBACK = 100002;
    public static final int TEST_4ACCRETION = 100003;
    public static final int TEST_4TARIFF_RETAIL = 100004;

    public static final NavigableMap<Integer, String> TEST_RESULTS = new TreeMap<>();

    //==========================================================================
    public static final void initializeTestMode() {

        synchronized (TestConst.class) {

            if (!TestConst.TEST_MODE_RUNNING) {
                TestConst.TEST_MODE_RUNNING = Boolean.TRUE;
            }
        }
    }

    //==========================================================================
    public static final void prepareTestException() {

        //TEST_RESULTS.put(testClass, SysConst.STRING_NULL);
        TEST_RESULTS.put(TEST_RESULTS.size() + 1, SysConst.STRING_NULL);
    }

    //==========================================================================
    public static final NavigableMap.Entry<Integer, String> getLastTestEntry() {
//        final Iterator<NavigableMap.Entry<Class, String>> iter = TEST_RESULTS.entrySet().iterator();
//        Map.Entry<Class, String> entry = null;
//        while (iter.hasNext()) {
//            entry = iter.next();
//        }
        return TEST_RESULTS.lastEntry();
    }

    //==========================================================================
    public static final void registerTestException(final String msgException) {

        final NavigableMap.Entry<Integer, String> entry = TestConst.getLastTestEntry();
        if (null == entry.getValue()) {
            TEST_RESULTS.replace(entry.getKey(), msgException);
        }
    }

    //==========================================================================
    public static final void resetTestException() {

        final NavigableMap.Entry<Integer, String> entry = TestConst.getLastTestEntry();
        if (null == entry.getValue()) {
            TEST_RESULTS.replace(entry.getKey(), SysConst.STRING_NULL);
        }
    }
}
