package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.DeveloperStatus;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum DeveloperStatusEnum {

    ET_1(1, "ES.1");

    public static final Collection<DeveloperStatus> DEVELOPERS_STATUSES_LIST = ServiceFuncs.<DeveloperStatus>createCollection(cp -> Arrays.stream(DeveloperStatusEnum.values())
            .map(stringRow -> create(DeveloperStatus.class, ref -> {
                ref.setDeveloperStatusId(stringRow.getDeveloperStatusId());
                ref.setDeveloperStatusName(stringRow.getDeveloperStatusName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> DEVELOPERS_STATUSES_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> DEVELOPERS_STATUSES_LIST.forEach(ref -> cp.add(ref.getDeveloperStatusId())));
    private final Integer developerStatusId;
    private final String developerStatusName;

}