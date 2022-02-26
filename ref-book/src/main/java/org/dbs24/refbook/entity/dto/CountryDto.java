package org.dbs24.refbook.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.refbook.entity.Country;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    private Integer countryId;
    private String countryIso;
    private String countryName;

    public static Country toCountry(CountryDto countryDto) {

        return Country.builder()
                .countryId(countryDto.getCountryId())
                .countryIso(countryDto.getCountryIso())
                .countryName(countryDto.getCountryName())
                .build();
    }

    public static CountryDto toCountryDto(Country country) {

        return CountryDto.builder()
                .countryId(country.getCountryId())
                .countryIso(country.getCountryIso())
                .countryName(country.getCountryName())
                .build();
    }
}
