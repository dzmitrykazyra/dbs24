package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Spammer;
import org.dbs24.stmt.StmtProcessor;

import java.util.List;

@Data
public class SpammerList {

    private List<Spammer> spammerList;

    public static SpammerList of(List<Spammer> spammers) {

        return StmtProcessor.create(
                SpammerList.class,
                dto -> dto.setSpammerList(spammers)
        );
    }
}
