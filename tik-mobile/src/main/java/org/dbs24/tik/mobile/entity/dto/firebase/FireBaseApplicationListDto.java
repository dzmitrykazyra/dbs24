package org.dbs24.tik.mobile.entity.dto.firebase;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.FireBaseApplication;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FireBaseApplicationListDto {

    private List<FireBaseApplicationInfoDto> fireBaseApplications;

    public static FireBaseApplicationListDto of(List<FireBaseApplication> fireBaseApplications) {

        return StmtProcessor.create(FireBaseApplicationListDto.class,
                applications -> applications.setFireBaseApplications(
                        fireBaseApplications
                                .stream()
                                .map(FireBaseApplicationInfoDto::of)
                                .collect(Collectors.toList())

                )
        );
    }

}
