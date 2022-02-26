package org.dbs24.health.component;

import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Data
public class Application {
    private String name;
    private String address;
    private String uri;
    private String rebootCmd;
    private WebClient webClient;
    private LocalDateTime nextAttemptTest;
    private Boolean isValid;
    private Boolean canReboot;
    private Integer legalAttepmts;
    private Integer remainAttepmts;
    private Integer allowedPing;
}
