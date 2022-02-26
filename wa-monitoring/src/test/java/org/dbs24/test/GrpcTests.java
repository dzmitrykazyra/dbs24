package org.dbs24.test;

import io.grpc.internal.testing.StreamRecorder;
import lombok.extern.log4j.Log4j2;
import org.dbs24.WaMonitoring;
import org.dbs24.config.WaServerConfig;
import org.dbs24.grpc.WaGrpcService;
import org.dbs24.wa.monitoring.grpc.Services;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.dbs24.stmt.StmtProcessor.execute;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {WaMonitoring.class},
        properties = {
                "grpc.server.inProcessName=test", // Enable inProcess server
                "grpc.server.port=-1", // Disable external server
                "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
        }
)
@Import({WaServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext // reboot context after each test
class GrpcTests extends AbstractGrpcTest {

    @Autowired
    private WaGrpcService.WaMainGrpcService waMainGrpcService;

    @Test
    @DisplayName("generic grpc test")
    void testSayHello() {

        runTest(() -> {

            Services.HelloRequest request = Services.HelloRequest.newBuilder()
                    .setName1("test")
                    .build();

            StreamRecorder<Services.HelloResponse> responseObserver = StreamRecorder.create();
            waMainGrpcService.sayHello(request, responseObserver);

            execute(() -> {
                if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
                    fail("The call did not terminate in time");
                }
            });

            log.debug("responseObserver: {}", responseObserver.getValues());

            assertNull(responseObserver.getError());

        });
    }
}
