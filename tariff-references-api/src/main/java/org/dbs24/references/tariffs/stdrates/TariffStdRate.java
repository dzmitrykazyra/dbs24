/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.stdrates;

import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import org.dbs24.references.api.AbstractRefRecord;

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

    public TariffStdRate setTariff_std_group_id( Integer tariff_std_group_id) {
        this.tariff_std_group_id = tariff_std_group_id;
        return this;
    }

    public Integer getTariff_std_rate_id() {
        return tariff_std_rate_id;
    }

    public TariffStdRate setTariff_std_rate_id( Integer tariff_std_rate_id) {
        this.tariff_std_rate_id = tariff_std_rate_id;
        return this;
    }

    public String getTariff_std_rate_name() {
        return tariff_std_rate_name;
    }

    public TariffStdRate setTariff_std_rate_name( String tariff_std_rate_name) {
        this.tariff_std_rate_name = tariff_std_rate_name;
        return this;
    }
}
