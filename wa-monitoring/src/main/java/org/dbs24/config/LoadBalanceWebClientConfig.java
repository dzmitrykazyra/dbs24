package org.dbs24.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.dbs24.consts.WaConsts.Common.APP_NAME;

@Configuration
@LoadBalancerClient(name = APP_NAME)
@ConditionalOnProperty(name = "eureka.balancer.enabled", havingValue = "true")
public class LoadBalanceWebClientConfig {

	@LoadBalanced
	@Bean
	WebClient.Builder webClientLoadBalanceBuilder() {
		return WebClient.builder();
	}

}
