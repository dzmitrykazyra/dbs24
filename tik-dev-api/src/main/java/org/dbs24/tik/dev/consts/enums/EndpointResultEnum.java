package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.EndpointResult;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum EndpointResultEnum {

    ET_1(1, "ES.1");

    public static final Collection<EndpointResult> ENDPOINTS_RESULTS_LIST = ServiceFuncs.<EndpointResult>createCollection(cp -> Arrays.stream(EndpointResultEnum.values())
            .map(stringRow -> create(EndpointResult.class, ref -> {
                ref.setEndpointResultId(stringRow.getId());
                ref.setEndpointResultName(stringRow.getEndpointResultName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> ENDPOINTS_RESULTS_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> ENDPOINTS_RESULTS_LIST.forEach(ref -> cp.add(ref.getEndpointResultId())));
    private final Integer id;
    private final String endpointResultName;

}