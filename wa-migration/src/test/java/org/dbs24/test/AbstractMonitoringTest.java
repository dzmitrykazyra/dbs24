/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.dbs24.repository.*;
import org.dbs24.component.*;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@TestPropertySource(properties = {
    "config.security.profile.webfilter.chain=development"})
public abstract class AbstractMonitoringTest extends AbstractWebTest {


}
