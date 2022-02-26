/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.test;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@Data
@Log4j2
@TestPropertySource(properties = {
    "config.security.profile.webfilter.chain=development"})
public abstract class AbstractInstaFsTest extends AbstractWebTest {

}
