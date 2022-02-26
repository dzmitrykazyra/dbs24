package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.ContractStatus;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum ContractStatusEnum {

    SC_ACTIVE(10000, "CS.ACTIVE"),
    SC_CLOSED(10100, "CS.CLOSED"),
    SC_CANCELLED(-1, "CS.CANCELLED");

    public static final Collection<ContractStatus> CONTRACTS_STATUSES_LIST = ServiceFuncs.<ContractStatus>createCollection(cp -> Arrays.stream(ContractStatusEnum.values())
            .map(stringRow -> create(ContractStatus.class, ref -> {
                ref.setContractStatusId(stringRow.getContractStatusId());
                ref.setContractStatusName(stringRow.getContractStatusName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> CONTRACTS_STATUSES_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> CONTRACTS_STATUSES_LIST.forEach(ref -> cp.add(ref.getContractStatusId())));
    private final Integer contractStatusId;
    private final String contractStatusName;

}