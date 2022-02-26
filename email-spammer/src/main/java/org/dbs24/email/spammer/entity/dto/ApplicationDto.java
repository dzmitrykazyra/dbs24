package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Application;
import org.dbs24.stmt.StmtProcessor;

@Data
public class ApplicationDto {

    private Integer applicationId;
    private String applicationTitle;

    public static ApplicationDto of(Application application) {

        return StmtProcessor.create(
                ApplicationDto.class,
                applicationDto -> {
                    applicationDto.setApplicationId(application.getApplicationId());
                    applicationDto.setApplicationTitle(application.getApplicationTitle());
                }
        );
    }
}
