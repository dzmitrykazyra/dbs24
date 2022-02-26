package org.dbs24.email.spammer.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.email.spammer.constant.ApiPath;
import org.dbs24.email.spammer.entity.dto.ApplicationListDto;
import org.dbs24.email.spammer.entity.dto.SpammerDto;
import org.dbs24.email.spammer.entity.dto.SpammerIdDto;
import org.dbs24.email.spammer.entity.dto.SubscriberDto;
import org.dbs24.email.spammer.rest.ApplicationRest;
import org.dbs24.email.spammer.rest.SpammerRest;
import org.dbs24.email.spammer.rest.SubscriberRest;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
public class EmailSpammerRouter extends AbstractWebSecurityConfig {

    private static final String SPAMMERS_SWAGGER_TAG = "Spammers";
    private static final String APPLICATIONS_SWAGGER_TAG = "Applications";
    private static final String SPAM_SENDING_SWAGGER_TAG = "Spam sending";
    private static final String SPAM_SUBSCRIBERS_SWAGGER_TAG = "Spam subscribers";

    @RouterOperations({
            @RouterOperation(
                    path = ApiPath.CREATE_SPAMMER,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = SPAMMERS_SWAGGER_TAG,
                            operationId = ApiPath.CREATE_SPAMMER,
                            requestBody = @RequestBody(
                                    description = "Spammer to create info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SpammerDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Created spammer info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SpammerIdDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = ApiPath.REGISTER_SUBSCRIBER,
                    method = RequestMethod.POST,
                    operation = @Operation(
                            tags = SPAM_SUBSCRIBERS_SWAGGER_TAG,
                            operationId = ApiPath.REGISTER_SUBSCRIBER,
                            requestBody = @RequestBody(
                                    description = "Spam subscriber to create DTO (application id can be found with its endpoint)",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SubscriberDto.class
                                            )
                                    )
                            ),
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Created spam subscriber info",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = SpammerIdDto.class
                                            )
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = ApiPath.GET_ALL_APPLICATIONS,
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = APPLICATIONS_SWAGGER_TAG,
                            operationId = ApiPath.GET_ALL_APPLICATIONS,
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "All applications list DTO",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = ApplicationListDto.class
                                            )
                                    )
                            )
                    )
            ),
    })
    @Bean
    public RouterFunction<ServerResponse> registerRoutes(SpammerRest spammerRest,
                                                         ApplicationRest applicationRest,
                                                         SubscriberRest subscriberRest) {
        return RouterFunctions
                .route(RequestPredicates.POST(ApiPath.CREATE_SPAMMER).and(accept(MediaType.APPLICATION_JSON)), spammerRest::createSpammer)

                .andRoute(RequestPredicates.POST(ApiPath.REGISTER_SUBSCRIBER).and(accept(MediaType.APPLICATION_JSON)), subscriberRest::createSubscriber)

                .andRoute(RequestPredicates.GET(ApiPath.GET_ALL_APPLICATIONS).and(accept(MediaType.APPLICATION_JSON)), applicationRest::getAllApplications)
                ;
    }
}
