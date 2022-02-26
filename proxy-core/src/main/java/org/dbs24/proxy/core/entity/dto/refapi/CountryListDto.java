package org.dbs24.proxy.core.entity.dto.refapi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CountryListDto {

    private List<CountryDto> countryDtoList;
}
