/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Козыро Дмитрий
 */
public class ComponentResourceTest {

    interface GreetingService {

        public String sayHello();
    }

    @Component(value = "real")
    class RealGreetingService implements GreetingService {

        @Override
        public String sayHello() {
            return "I'm real";
        }
    }

    @Component(value = "mock")
    class MockGreetingService implements GreetingService {

        @Override
        public String sayHello() {
            return "I'm mock";
        }
    }
    //application.properties
    //application.greeting: real 
    @Resource(name = "${application.greeting:real}")
    private GreetingService greetingService;
}
