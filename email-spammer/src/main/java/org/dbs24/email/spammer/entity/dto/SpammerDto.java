package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Spammer;
import org.dbs24.stmt.StmtProcessor;

@Data
public class SpammerDto {

    private String email;
    private Boolean isActive;
    private String password;

    public Spammer toSpammer() {

        return StmtProcessor.create(
                Spammer.class,
                spammer -> {
                    spammer.setSpammerEmail(this.getEmail());
                    spammer.setIsActive(this.getIsActive());
                    spammer.setPassword(this.getPassword());
                }
        );
    }

    public static SpammerDto of(Spammer spammer) {

        return StmtProcessor.create(
                SpammerDto.class,
                spammerDto -> {
                    spammerDto.setEmail(spammer.getSpammerEmail());
                    spammerDto.setIsActive(spammer.getIsActive());
                    spammerDto.setPassword(spammer.getPassword());
                }
        );
    }
}
