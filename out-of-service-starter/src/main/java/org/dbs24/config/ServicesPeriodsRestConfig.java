package org.dbs24.config;


import org.dbs24.rest.ServicePeriodRest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.dbs24.consts.ServicePeriodsConsts.Routes.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ServicesPeriodsRestConfig extends AbstractWebSecurityConfig {

    final ServicePeriodRest servicePeriodRest;

    public ServicesPeriodsRestConfig(ServicePeriodRest servicePeriodRest) {
        this.servicePeriodRest = servicePeriodRest;
    }

    public RouterFunction<ServerResponse> servicesPeriodRoutes() {
        return route(postRoute(ROUTE_OUT_OF_SERVICE_SET), servicePeriodRest::createOrUpdateServicePeriod)
                .andRoute(getRoute(ROUTE_OUT_OF_SERVICE_GET), servicePeriodRest::getPeriodsService);
    }
}
