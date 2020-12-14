/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.stdrategroup;

import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;

public class TariffStdRatesGroup extends AbstractRefRecord implements ReferenceRec {

    private Integer tariff_std_group_id;
    private String tariff_std_group_name;

    public TariffStdRatesGroup() {
        super();
    }

    public TariffStdRatesGroup( Integer tariff_std_group_id, String tariff_std_group_name) {
        this();
        this.tariff_std_group_id = tariff_std_group_id;
        this.tariff_std_group_name = tariff_std_group_name;
    }

    //==========================================================================
    public Integer getTariff_std_group_id() {
        return tariff_std_group_id;
    }

    public TariffStdRatesGroup setTariff_std_group_id( Integer tariff_std_group_id) {
        this.tariff_std_group_id = tariff_std_group_id;
        return this;
    }

    public String getTariff_std_group_name() {
        return tariff_std_group_name;
    }

    public TariffStdRatesGroup setTariff_std_group_name( String tariff_std_group_name) {
        this.tariff_std_group_name = tariff_std_group_name;
        return this;
    }
}
