package org.dbs24.refbook.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CountryListDto {

    private List<CountryDto> countryDtoList;
}
