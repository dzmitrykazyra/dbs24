package org.dbs24.grpc;

import io.grpc.stub.StreamObserver;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;
import org.dbs24.service.AbstractGrpcService;
import org.dbs24.wa.monitoring.grpc.Services;
import org.dbs24.wa.monitoring.grpc.WaMonitoringServiceGrpc;
import org.springframework.stereotype.Component;

@Log4j2
@EqualsAndHashCode(callSuper = true)
@Component
public class WaGrpcService extends AbstractGrpcService {

    @GrpcService
    public class WaMainGrpcService extends WaMonitoringServiceGrpc.WaMonitoringServiceImplBase {


        public void sayHello(Services.HelloRequest req, StreamObserver<Services.HelloResponse> responseObserver) {
            Services.HelloResponse reply = Services.HelloResponse.newBuilder().setMessage("Hello ==> " + req.getName1()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

    }

}
