/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.RetailLoanContractBoot;
import org.springframework.context.annotation.Import;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class TestRetailLoanContractBootApp extends RetailLoanContractBoot {

}
