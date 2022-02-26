package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.service.mail.Email;
import reactor.core.publisher.Mono;

public interface EmailSendingService {

    Mono<Void> send(Email email);
}
