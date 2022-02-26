package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Spammer;
import org.dbs24.stmt.StmtProcessor;

@Data
public class SpammerIdDto {

    private Integer spammerId;

    public static SpammerIdDto of(Spammer spammer) {

        return StmtProcessor.create(
                SpammerIdDto.class,
                spammerIdDto -> spammerIdDto.setSpammerId(spammer.getSpammerId())
        );
    }
}
