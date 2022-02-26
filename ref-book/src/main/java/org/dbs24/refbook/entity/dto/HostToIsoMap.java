package org.dbs24.refbook.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class HostToIsoMap {

    private Map<String, String> hostToCountry = new HashMap<>();

    public void put(String host, String countryIso) {

        hostToCountry.put(host, countryIso);
    }

    public String get(String host) {

        return hostToCountry.get(host);
    }
}
