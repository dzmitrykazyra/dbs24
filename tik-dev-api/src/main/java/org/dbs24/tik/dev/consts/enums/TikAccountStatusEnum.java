package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.TikAccountStatus;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum TikAccountStatusEnum {

    ET_1(1, "ES.1");

    public static final Collection<TikAccountStatus> TIK_ACCOUNT_STATUSES_LIST = ServiceFuncs.<TikAccountStatus>createCollection(cp -> Arrays.stream(TikAccountStatusEnum.values())
            .map(stringRow -> create(TikAccountStatus.class, ref -> {
                ref.setTikAccountStatusId(stringRow.getAccountStatusId());
                ref.setTikAccountStatusName(stringRow.getAccountStatusName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> TIK_ACCOUNT_STATUSES_IDS = ServiceFuncs.<Integer>createCollection(cp -> TIK_ACCOUNT_STATUSES_LIST.forEach(ref -> cp.add(ref.getTikAccountStatusId())));
    private final Integer accountStatusId;
    private final String accountStatusName;

}