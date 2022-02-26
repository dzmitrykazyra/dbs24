package org.dbs24.email.spammer.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.email.spammer.entity.domain.Application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ApplicationDefine {

    TIKTOK_WEB(1, "TIKTOKWEB");

    public static List<Application> getAllApplications() {

        return Arrays.stream(ApplicationDefine.values())
                .map(
                        applicationDefine -> Application.builder()
                                .applicationId(applicationDefine.getApplicationId())
                                .applicationTitle(applicationDefine.getApplicationTitle())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private Integer applicationId;
    private String applicationTitle;
}
