/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.StringFuncs;
import org.dbs24.exception.BadEqualsAndHashCodeConfig;

import java.util.Random;

import static java.lang.String.format;
import static org.dbs24.stmt.StmtProcessor.ifNotNull;

@Data
@Log4j2
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class AbstractApplicationBean implements ApplicationBean {

    @EqualsAndHashCode.Include
    private String internalServiceId = StringFuncs.createRandomString(20);

    final Random random = new Random();

    final ThrowableProcessor tp = th -> {

        log.error("### Exception: '{}'", th.getMessage());
        ifNotNull(th, exception -> exception.printStackTrace(), () -> log.error("### throwable object is invalid")

        );
    };

    @Override
    public void initialize() {
        final String className = this.getClass().getSimpleName();
        log.info("Microservice '{}' is created", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    @Override
    public void shutdown() {
        log.info("{}: Shutting down", this.getClass().getSimpleName());
    }

    public boolean getTest4BadEqualsAndHashCode() {
        if (true == true)
            throw new BadEqualsAndHashCodeConfig(format("%s: Equals and hashcode is not correctly defined", this.getClass().getCanonicalName()));
        return true;
    }
}
