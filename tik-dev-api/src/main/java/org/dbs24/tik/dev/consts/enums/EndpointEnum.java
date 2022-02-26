package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.Endpoint;

import java.util.Arrays;
import java.util.Collection;

import static java.time.LocalDateTime.now;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.tik.dev.consts.enums.EndpointScopeEnum.ENDPOINTS_SCOPES_LIST;
import static org.dbs24.tik.dev.consts.enums.EndpointScopeEnum.VIEW_PROFILE;


@Getter
@AllArgsConstructor
public enum EndpointEnum {

    EP1(100100, VIEW_PROFILE, "uri1", BOOLEAN_TRUE, "descr1"),
    EP2(100101, VIEW_PROFILE, "uri2", BOOLEAN_TRUE, "descr2");

    public static final Collection<Endpoint> ENDPOINTS_LIST = ServiceFuncs.<Endpoint>createCollection(cp -> Arrays.stream(EndpointEnum.values())
            .map(stringRow -> create(Endpoint.class, ref -> {
                ref.setEndpointRefId(stringRow.getEndpointRefId());
                ref.setCreateDate(now());
                ref.setEndpointScope(ENDPOINTS_SCOPES_LIST.stream().filter(es -> stringRow.getEndpointScope().getId().equals(es.getEndpointScopeId())).findFirst().orElseThrow());
                ref.setIsActual(stringRow.getIsActual());
                ref.setDescription(stringRow.getDescription());
                ref.setUri(stringRow.getUri());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> ENDPOINTS_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> ENDPOINTS_LIST.forEach(ref -> cp.add(ref.getEndpointRefId())));
    private final Integer endpointRefId;
    private final EndpointScopeEnum endpointScope;
    private final String uri;
    private final Boolean isActual;
    private final String description;

}