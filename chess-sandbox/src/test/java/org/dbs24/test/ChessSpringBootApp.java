/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.ChessSandBoxBoot;

import static org.dbs24.consts.SysConst.*;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ChessSpringBootApp extends ChessSandBoxBoot {
    
}
