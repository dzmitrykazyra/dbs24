package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Application;
import org.dbs24.stmt.StmtProcessor;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ApplicationListDto {

    private List<ApplicationDto> applicationDtos;

    public static ApplicationListDto of(List<Application> applicationList) {

        return StmtProcessor.create(
                ApplicationListDto.class,
                applicationListDto -> applicationListDto.setApplicationDtos(
                        applicationList.stream()
                                .map(ApplicationDto::of)
                                .collect(Collectors.toList())
                )
        );
    }
}
