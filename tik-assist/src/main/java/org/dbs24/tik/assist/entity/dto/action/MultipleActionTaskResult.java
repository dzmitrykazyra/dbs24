package org.dbs24.tik.assist.entity.dto.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MultipleActionTaskResult {

    @JsonProperty("tasks")
    private List<ActionTaskResult> actionTaskResults;

    @JsonProperty("all_fail")
    private boolean isAllFailed;
}
