package org.dbs24.proxy.core.entity.dto.response.body;

import lombok.Data;
import org.dbs24.proxy.core.entity.dto.ApplicationDto;
import org.dbs24.spring.core.api.EntityInfo;

import java.util.List;

@Data
public class ApplicationDtoList implements EntityInfo {

    private List<ApplicationDto> applicationDtoList;
}