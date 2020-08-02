/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.log;

/**
 *
 * @author Козыро Дмитрий
 */

import com.kdg.fs24.application.core.nullsafe.NullSafe;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.util.Properties;

public final class Log4jBox {

    private final String log4file = System.getProperty("log4j.configuration");
    private final Properties log4jProperties = NullSafe.createObject(Properties.class);

    public Log4jBox() {

        NullSafe.create(this.log4file)
                .whenIsNull(() -> {
                    throw new RuntimeException(" log4j.configuration is not defined (-Dlog4j.configuration) ");
                })
                .execute(() -> {

                    //this.log4file = "d:\\fs24\\log4j.properties";
                    PropertyConfigurator.configure(log4file);

                    log4jProperties.load(new FileInputStream(log4file));
                }).throwException();
    }

    public Properties getProperties() {
        return log4jProperties;
    }
}
