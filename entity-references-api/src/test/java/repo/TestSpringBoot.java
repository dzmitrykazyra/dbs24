/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.spring.unit.SpringBoot4Test;
import java.util.Collection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import com.kdg.fs24.application.core.sysconst.SysConst;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication
@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
@PropertySource(SysConst.APP_PROPERTIES)
@EntityScan(basePackages = {SysConst.ENTITY_PACKAGE})
@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
public class TestSpringBoot extends SpringBoot4Test {

}
