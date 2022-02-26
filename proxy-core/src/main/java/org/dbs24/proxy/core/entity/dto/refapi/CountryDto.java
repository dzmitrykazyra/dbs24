package org.dbs24.proxy.core.entity.dto.refapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.proxy.core.entity.domain.Country;
import org.dbs24.stmt.StmtProcessor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    private Integer countryId;
    private String countryIso;
    private String countryName;

    public static Country toCountry(CountryDto countryDto) {

        return StmtProcessor.create(
                Country.class,
                country -> {
                    country.setCountryId(countryDto.getCountryId());
                    country.setCountryIso(countryDto.getCountryIso());
                    country.setCountryName(countryDto.getCountryName());
                }
        );
    }

    public static CountryDto toCountryDto(Country country) {

        return CountryDto.builder()
                .countryId(country.getCountryId())
                .countryIso(country.getCountryIso())
                .countryName(country.getCountryName())
                .build();
    }
}
