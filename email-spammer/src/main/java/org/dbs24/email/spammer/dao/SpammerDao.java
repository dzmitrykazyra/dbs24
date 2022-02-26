package org.dbs24.email.spammer.dao;

import org.dbs24.email.spammer.entity.domain.Spammer;

import java.util.List;
import java.util.Optional;

public interface SpammerDao {

    Spammer save(Spammer spammer);

    Optional<Spammer> findOptionalByEmail(String email);
    List<Spammer> findAll();
}
