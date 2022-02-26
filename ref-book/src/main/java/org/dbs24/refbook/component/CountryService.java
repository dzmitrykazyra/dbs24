package org.dbs24.refbook.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.refbook.dao.MmdbDao;
import org.dbs24.refbook.entity.Country;
import org.dbs24.refbook.entity.dto.*;
import org.dbs24.refbook.repo.CountryRepo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Data
@Log4j2
@Component
public class CountryService extends AbstractApplicationService {

    final CountryRepo countryRepo;

    final MmdbDao mmdbDao;

    public CountryService(CountryRepo countryRepo, MmdbDao mmdbDao) {

        this.countryRepo = countryRepo;
        this.mmdbDao = mmdbDao;
    }

    public CreatedCountryDto createCountry(CountryDto countryDto) {

        Country savedCountry = countryRepo.save(StmtProcessor.create(
                Country.class,
                country -> CountryDto.toCountry(countryDto)
        ));

        return StmtProcessor.create(
                CreatedCountryDto.class,
                createdCountryDto -> createdCountryDto.setCountryIso(savedCountry.getCountryIso())
        );
    }

    public CreatedCountryListDto createCountries(CountryListDto countryListDto) {

        return new CreatedCountryListDto(
                countryRepo.saveAllAndFlush(
                        countryListDto
                                .getCountryDtoList()
                                .stream()
                                .map(CountryDto::toCountry)
                                .collect(Collectors.toList()))
                        .stream()
                        .map(
                                country -> StmtProcessor.create(
                                        CreatedCountryDto.class,
                                        createdCountryDto -> createdCountryDto.setCountryIso(country.getCountryIso())
                                ))
                        .collect(Collectors.toList()));
    }

    public CountryListDto getAllCountries() {

        CountryListDto countryListDto1 = StmtProcessor.create(
                CountryListDto.class,
                countryListDto -> countryListDto.setCountryDtoList(
                        countryRepo
                                .findAll()
                                .stream()
                                .map(CountryDto::toCountryDto)
                                .collect(Collectors.toList())
                ));

        return countryListDto1;
    }

    public CountryDto getCountry(String countryIso) {

        return StmtProcessor.create(
                CountryDto.class,
                countryDto -> countryRepo.findByCountryIso(countryIso).ifPresent(CountryDto::toCountryDto)
        );
    }

    public HostToIsoMap getHostToCountryByHostList(HostListDto hostListDto) {
        log.info("HOST LIST SIZE : {}", hostListDto.getHostList().size());

        return StmtProcessor.create(
                HostToIsoMap.class,
                hostToIsoMap -> hostToIsoMap.setHostToCountry(
                        mmdbDao.getHostToCountryByHostList(
                                hostListDto.getHostList()
                        )
                )
        );
    }
}
