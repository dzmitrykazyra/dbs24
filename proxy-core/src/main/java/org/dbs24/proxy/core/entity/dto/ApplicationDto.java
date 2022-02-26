package org.dbs24.proxy.core.entity.dto;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;

@Data
public class ApplicationDto implements EntityInfo {
    private Integer applicationId;
    private String name;
    private String description;
    private String applicationNetworkName;
    private Integer applicationStatusId;

    public static ApplicationDto of(Application application) {

        return StmtProcessor.create(ApplicationDto.class, applicationDto -> {
            applicationDto.setApplicationId(application.getApplicationId());
            applicationDto.setName(application.getName());
            applicationDto.setDescription(application.getDescription());
            applicationDto.setApplicationNetworkName(application.getApplicationNetwork().getApplicationNetworkName());
            applicationDto.setApplicationStatusId(application.getApplicationStatus().getApplicationStatusId());
        });
    }
}
