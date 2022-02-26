/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.test.context.TestPropertySource;

@Data
@Log4j2
@TestPropertySource(properties = {
    "config.security.profile.webfilter.chain=development",
    "springdoc.api-docs.enabled=false",
    "springdoc.swagger-ui.enabled=false"})
public abstract class AbstractPaymentTest extends AbstractWebTest {

}
