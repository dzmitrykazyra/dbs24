/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test.core;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public abstract class AbstractRSocketTest extends AbstractTest {

//    @Autowired
//    AbstractRSocketService rSocketService;
//
//    @Value("${spring.rsocket.server.port:6666}")
//    protected Integer rSocketPort;
//
//    @Value("${spring.rsocket.server.test.block.delay:600000}") // ms
//    protected Integer blockDelay;
//
//    private RSocket rSocket;
//
//    @BeforeAll
//    public void setUpClient() {
//
//        this.rSocket = RSocketConnector.connectWith(TcpClientTransport.create(rSocketPort)).block();
//
//        log.debug("RSocket 4 test is ready ({})", rSocket.getClass().getCanonicalName());
//    }
//
//    @AfterAll
//    public void finishClient() {
//
//        //this.rSocket = (AbstractRSocket) RSocketConnector.connectWith(TcpClientTransport.create(rSocketPort)).block();
//        this.rSocket.dispose();
//        log.debug("RSocket 4 test is stopped ({})", rSocket.getClass().getCanonicalName());
//    }
}
