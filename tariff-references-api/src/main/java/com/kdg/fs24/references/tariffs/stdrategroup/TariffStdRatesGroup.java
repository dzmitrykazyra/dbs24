/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.stdrategroup;

import com.kdg.fs24.references.api.ReferenceRec;
import java.util.Map;
import com.kdg.fs24.references.api.AbstractRefRecord;

/**
 *
 * @author kazyra_d
 */
public class TariffStdRatesGroup extends AbstractRefRecord implements ReferenceRec {

    private Integer tariff_std_group_id;
    private String tariff_std_group_name;

    public TariffStdRatesGroup() {
        super();
    }

    public TariffStdRatesGroup(final Integer tariff_std_group_id, final String tariff_std_group_name) {
        this();
        this.tariff_std_group_id = tariff_std_group_id;
        this.tariff_std_group_name = tariff_std_group_name;
    }

    //==========================================================================
    public Integer getTariff_std_group_id() {
        return tariff_std_group_id;
    }

    public TariffStdRatesGroup setTariff_std_group_id(final Integer tariff_std_group_id) {
        this.tariff_std_group_id = tariff_std_group_id;
        return this;
    }

    public String getTariff_std_group_name() {
        return tariff_std_group_name;
    }

    public TariffStdRatesGroup setTariff_std_group_name(final String tariff_std_group_name) {
        this.tariff_std_group_name = tariff_std_group_name;
        return this;
    }

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getTariff_std_group_id(), this.getTariff_std_group_name()), this.getTariff_std_group_id());
    }

}
