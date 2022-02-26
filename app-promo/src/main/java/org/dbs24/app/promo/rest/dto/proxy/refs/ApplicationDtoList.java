package org.dbs24.app.promo.rest.dto.proxy.refs;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

@Data
public class ApplicationDtoList implements EntityInfo {
    private List<ApplicationDto> applicationDtoList;
}
