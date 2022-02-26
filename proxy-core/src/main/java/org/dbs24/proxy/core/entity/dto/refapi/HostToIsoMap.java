package org.dbs24.proxy.core.entity.dto.refapi;

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
