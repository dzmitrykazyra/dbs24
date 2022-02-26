package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepairTaskResultDefine {

    TR_CREATED(0),
    TR_FINISHED(1),
    TR_NOT_STARTED(2),
    TR_IN_PROGRESS(3),
    TR_TRY_LATER(-1),
    TR_AGENT_IS_BANNED(-2);

    private final Integer id;
}