package org.dbs24.tik.dev.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.dev.entity.reference.EndpointScope;

import java.util.Arrays;
import java.util.Collection;

import static org.dbs24.stmt.StmtProcessor.create;


@Getter
@AllArgsConstructor
public enum EndpointScopeEnum {

    VIEW_PROFILE(10, "view_profile"),
    EDIT_PROFILE(20, "edit_profile"),
    VIEW_MESSAGES(30, "view_messages"),
    MEDIA_ACTIONS(40, "media_actions"),
    FOLLOW_ACTIONS(50, "follow_actions"),
    LIVE(60, "live"),
    SEARCH(70, "search");

    public static final Collection<EndpointScope> ENDPOINTS_SCOPES_LIST = ServiceFuncs.<EndpointScope>createCollection(cp -> Arrays.stream(EndpointScopeEnum.values())
            .map(stringRow -> create(EndpointScope.class, ref -> {
                ref.setEndpointScopeId(stringRow.getId());
                ref.setEndpointScopeName(stringRow.getEndpointScopeName());
            })).forEach(cp::add)
    );
    public static final Collection<Integer> ENDPOINTS_SCOPES_LIST_IDS = ServiceFuncs.<Integer>createCollection(cp -> ENDPOINTS_SCOPES_LIST.forEach(ref -> cp.add(ref.getEndpointScopeId())));
    private final Integer id;
    private final String endpointScopeName;

}