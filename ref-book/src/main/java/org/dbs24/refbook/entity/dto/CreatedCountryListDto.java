package org.dbs24.refbook.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatedCountryListDto {

    private List<CreatedCountryDto> createdCountryDtoList;
}
