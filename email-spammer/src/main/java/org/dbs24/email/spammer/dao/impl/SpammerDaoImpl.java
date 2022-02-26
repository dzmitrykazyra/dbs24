package org.dbs24.email.spammer.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.dao.SpammerDao;
import org.dbs24.email.spammer.entity.domain.Spammer;
import org.dbs24.email.spammer.repo.SpammerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class SpammerDaoImpl implements SpammerDao {

    private final SpammerRepository spammerRepository;

    public SpammerDaoImpl(SpammerRepository spammerRepository) {

        this.spammerRepository = spammerRepository;
    }

    @Override
    public Spammer save(Spammer spammer) {

        return spammerRepository.save(spammer);
    }

    @Override
    public Optional<Spammer> findOptionalByEmail(String email) {

        return spammerRepository.findBySpammerEmail(email);
    }

    @Override
    public List<Spammer> findAll() {

        return spammerRepository.findAll();
    }
}
