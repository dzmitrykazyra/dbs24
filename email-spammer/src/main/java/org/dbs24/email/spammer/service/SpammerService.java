package org.dbs24.email.spammer.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.dao.SpammerDao;
import org.dbs24.email.spammer.entity.domain.Spammer;
import org.dbs24.email.spammer.entity.dto.SpammerDto;
import org.dbs24.email.spammer.entity.dto.SpammerIdDto;
import org.dbs24.email.spammer.entity.dto.SpammerList;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Component
public class SpammerService {

    private final SpammerDao spammerDao;

    public SpammerService(SpammerDao spammerDao) {

        this.spammerDao = spammerDao;
    }

    public Mono<SpammerList> getAllSpammers() {

        return Mono.just(SpammerList.of(spammerDao.findAll()));
    }

    public Mono<SpammerIdDto> createSpammer(Mono<SpammerDto> spammerDtoMono) {

        return spammerDtoMono.map(
                spammerDto -> SpammerIdDto.of(
                        spammerDao.findOptionalByEmail(spammerDto.getEmail()).orElseGet(() ->
                                spammerDao.save(
                                        spammerDto.toSpammer()
                                )
                        )
                )
        );
    }
}
