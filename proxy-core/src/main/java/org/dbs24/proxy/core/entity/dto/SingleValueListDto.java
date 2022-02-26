package org.dbs24.proxy.core.entity.dto;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

/**
 * DTO containing list of values. Can be used, for example, to return entities from ..._ref tables
 */
@Data
public class SingleValueListDto implements EntityInfo {

    private List<String> referenceValues;
}
