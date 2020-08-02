/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.stdrates;

import com.kdg.fs24.references.api.ReferenceRec;
import java.util.Map;
import com.kdg.fs24.references.api.AbstractRefRecord;

/**
 *
 * @author kazyra_d
 */
public class TariffStdRate extends AbstractRefRecord implements ReferenceRec {

    private Integer tariff_std_rate_id;
    private Integer tariff_std_group_id;
    private String tariff_std_rate_name;

    public TariffStdRate() {
        super();
    }

    //==========================================================================
    public Integer getTariff_std_group_id() {
        return tariff_std_group_id;
    }

    public TariffStdRate setTariff_std_group_id(final Integer tariff_std_group_id) {
        this.tariff_std_group_id = tariff_std_group_id;
        return this;
    }

    public Integer getTariff_std_rate_id() {
        return tariff_std_rate_id;
    }

    public TariffStdRate setTariff_std_rate_id(final Integer tariff_std_rate_id) {
        this.tariff_std_rate_id = tariff_std_rate_id;
        return this;
    }

    public String getTariff_std_rate_name() {
        return tariff_std_rate_name;
    }

    public TariffStdRate setTariff_std_rate_name(final String tariff_std_rate_name) {
        this.tariff_std_rate_name = tariff_std_rate_name;
        return this;
    }

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getTariff_std_rate_id(), this.getTariff_std_rate_name()), this.getTariff_std_rate_id());
    }

}
