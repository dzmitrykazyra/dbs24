package org.dbs24.impl.api;

import lombok.Data;
import org.dbs24.kafka.RegistryApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Data
public class ServerState extends RegistryApplication {

    private Long pid;
    private Integer usersProceed;
    private LocalDateTime lastProceed;
    private Boolean isReady;
    private WebClient webClient;
    private Integer attemptRetry;
    private Boolean isShoutDown;
    private String note;

}
