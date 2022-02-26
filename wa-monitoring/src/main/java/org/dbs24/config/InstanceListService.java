package org.dbs24.config;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Log4j2
@Service
@EqualsAndHashCode(callSuper = true)
@ConditionalOnProperty(name = "eureka.balancer.enabled", havingValue = "true")
public class InstanceListService extends AbstractApplicationService implements ServiceInstanceListSupplier {

    @Value("${eureka.service-name:not-defined}")
    private String serviceName;

    final DiscoveryClient discoveryClient;

    public InstanceListService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public String getServiceId() {
        return serviceName;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {

        var currentInstances = discoveryClient.getInstances(serviceName);

        log.debug("{}: current pod instances: (size = {}, pod instances = {})", serviceName, currentInstances.size(), currentInstances);

        return Flux.just(currentInstances);

    }

    @Override
    public void initialize() {
        super.initialize();

        var allInstances = discoveryClient.getInstances(serviceName);

        log.debug("{}: all services: {}, pod instances: (size = {}, pod instances = {})",
                serviceName,
                discoveryClient.getServices(),
                allInstances.size(),
                allInstances);
    }
}
