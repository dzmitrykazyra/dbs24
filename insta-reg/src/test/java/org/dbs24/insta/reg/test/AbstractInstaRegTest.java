/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.insta.reg.component.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Log4j2
@TestPropertySource(properties = {
    "config.security.profile.webfilter.chain=development",
    "config.accounts.processing-interval=",
    "config.accounts.processing-cron=-"})
public abstract class AbstractInstaRegTest extends AbstractWebTest {

    @Autowired
    private EmailService emailService;

}
