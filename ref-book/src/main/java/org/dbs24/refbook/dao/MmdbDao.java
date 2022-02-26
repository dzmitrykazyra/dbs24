package org.dbs24.refbook.dao;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.refbook.entity.Country;
import org.dbs24.refbook.repo.CountryRepo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@Data
@Log4j2
@Component
public class MmdbDao {

    private final String FILE_PATH = "/root/zeta24/ref-book/mmdb/GeoLite2-City.mmdb";
    private final File mmdb;
    private DatabaseReader reader;

    final CountryRepo countryRepo;

    public MmdbDao(CountryRepo countryRepo) throws Exception {
        this.countryRepo = countryRepo;

        mmdb = new File(FILE_PATH);
        reader = new DatabaseReader.Builder(mmdb).build();
    }

    public Map<String, String> getHostToCountryByHostList(List<String> hostList) {

        Map<String, String> hostToCountry = new HashMap<>();
        List<Country> countryEntityList = new ArrayList<>(hostList.size());

        hostList.forEach(host -> {
            Optional<Country> countryByHostOptional = Optional.empty();

            try {
                InetAddress ipAddress = InetAddress.getByName(host);
                CityResponse cityResponse = reader.city(ipAddress);
                com.maxmind.geoip2.record.Country countryFound = cityResponse.getCountry();

                Country countryEntity = Country.builder()
                        .countryId(countryFound.getGeoNameId())
                        .countryName(countryFound.getName())
                        .countryIso(countryFound.getIsoCode())
                        .build();

//                countryEntityList.add(countryEntity);

                countryByHostOptional = Optional.of(countryEntity);
            } catch (IOException | GeoIp2Exception e) {
                e.printStackTrace();
            }

            countryByHostOptional.ifPresent(country -> hostToCountry.put(host, country.getCountryIso()));
        });

        refreshCountryRepo(countryEntityList);

        return hostToCountry;
    }

    //todo remove later, when country list wil be full
    private void refreshCountryRepo(List<Country> countries) {

        countryRepo.saveAllAndFlush(countries);
    }
}
