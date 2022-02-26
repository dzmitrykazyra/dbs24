/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;
import java.util.Locale;

@Log4j2
@Service
public class JavaFakerService extends AbstractApplicationService {

    Faker faker = new Faker(new Locale("en_US"));

    public void setLocale(String locale){
        if (locale != null){
            faker = new Faker(new Locale(locale));
            log.info("Change locale to: {}", locale);
        }
    }
    public String createLastName() {
        return faker.name().lastName().substring(0,1).concat(faker.name().lastName().substring(1));
    }

    public String createFirstName() {
        return faker.name().firstName().substring(0,1).concat(faker.name().firstName().substring(1));
    }

    public String createPassword() {

        return faker.name().firstName().substring(0,1).concat(faker.internet().password(true));
    }

    public String generateRandomNumber(){
        return faker.bothify("##");
    }

    public String createGmail(){ return (faker.bothify("???????##"));}

    public String createUserAgent() {

        String newUserAgent;

        do {

            newUserAgent = faker.internet().userAgentAny();

        } while (newUserAgent.length() < 120);

        return newUserAgent;
    }

}

