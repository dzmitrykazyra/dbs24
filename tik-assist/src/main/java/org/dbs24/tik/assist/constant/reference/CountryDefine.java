package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.Country;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CountryDefine {

    BY(112, "BY", "Belarus"),
    RU(643, "RU", "Russian"),
    KZ(644, "KZ", "Kazakhstan");

    private final Integer id;
    private final String iso;
    private final String countryName;

    public static List<Country> getAll() {
        return Arrays.stream(CountryDefine.values()).map(
                countryEnum -> StmtProcessor.create(
                        Country.class,
                        country -> {
                            country.setCountryId(countryEnum.getId());
                            country.setCountryIso(countryEnum.getIso());
                            country.setCountryName(countryEnum.getCountryName());
                        }
                )
        ).collect(Collectors.toList());
    }
}