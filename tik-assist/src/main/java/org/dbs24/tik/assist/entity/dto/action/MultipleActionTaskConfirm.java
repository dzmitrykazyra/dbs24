package org.dbs24.tik.assist.entity.dto.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MultipleActionTaskConfirm {

    @JsonProperty("tasks")
    private List<ActionTaskConfirm> multipleActionTaskConfirm;

    @JsonProperty("all_fail")
    private boolean isAllFailed;
}
