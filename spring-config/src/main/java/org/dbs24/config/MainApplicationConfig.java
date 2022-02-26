/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.exception.MissingNecessaryBeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;

@Log4j2
@Getter
@ComponentScan(basePackages = {ALL_PACKAGES})
@EntityScan(basePackages = {ALL_PACKAGES})
@EqualsAndHashCode(callSuper = false)
public abstract class MainApplicationConfig extends AbstractApplicationConfiguration {

    public static GenericApplicationContext genericApplicationContext;
    final String className = this.getClass().getSimpleName();

    @Autowired
    public void initializeContext(GenericApplicationContext genericApplicationContext) {
        MainApplicationConfig.genericApplicationContext = genericApplicationContext;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        log.info("Try 2 create ObjectMapper ...");

        final ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // игнорируем ненужные поля
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, BOOLEAN_FALSE);

        log.info("ObjectMapper was created");

        return objectMapper;
    }

    //==========================================================================
    public void assertBeans(Class<?> clazz, String... beansList) {

        final Collection<String> defs = Arrays.asList(genericApplicationContext.getBeanDefinitionNames());

        final Collection<String> badBeansList = Stream.of(beansList)
                .filter(el -> !defs.contains(el))
                .collect(Collectors.toList());

        if (!badBeansList.isEmpty()) {

            throw new MissingNecessaryBeansException(String.format("%s: bad application configuration! (application require following bean(s): %s)",
                    clazz.getCanonicalName(),
                    badBeansList
                            .stream()
                            .reduce(String.format(" (%d): ",
                                            badBeansList.size()),
                                    (x, y) -> x.concat(", ").concat(y))));
        } else {
            log.info(Arrays.stream(beansList)
                    .sorted()
                    .reduce(String.format("Spring beans list (%d): \n",
                                    beansList.length),
                            (x, y) -> {
                                final BeanDefinition bd = genericApplicationContext.getBeanDefinition(y);
                                final String bcn = String.format("%s: %s [%s]",
                                        y,
                                        bd.getClass(),
                                        bd.getScope());
                                return x.concat("\n").concat(bcn);
                            }));
        }
    }
    //==================================================================================================================
    @Override
    public void initialize() {
        log.info("Configuration '{}' is activated", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }
}
