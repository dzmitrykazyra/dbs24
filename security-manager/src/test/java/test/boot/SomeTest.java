/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.boot;

import org.dbs24.application.core.log.LogService;
import org.junit.Test;

/**
 *
 * @author Козыро Дмитрий
 */
public final class SomeTest {

    @Test
    public void test3() {
        //this.initializeTest();
        LogService.LogInfo(this.getClass(), () -> String.format("Unit test '%s' is running ",
                this.getClass().getCanonicalName()));
    }

    @Test
    public void test4() {
        //this.initializeTest();
        LogService.LogInfo(this.getClass(), () -> String.format("Unit test '%s' is running ",
                this.getClass().getCanonicalName()));
    }

}
