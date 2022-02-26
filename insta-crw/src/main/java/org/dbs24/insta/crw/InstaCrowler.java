/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.crw;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InstaCrowler extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                InstaCrowler.class,
                EMPTY_INITIALIZATION);
    }
}
