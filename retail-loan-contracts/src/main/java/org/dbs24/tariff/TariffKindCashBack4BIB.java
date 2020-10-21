/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tariff;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.consts.TariffConst;
import org.dbs24.entity.tariff.TariffKindService;
import org.dbs24.references.tariffs.kind.TariffKindId;
import org.dbs24.entity.tariff.TariffRate_1;
import org.dbs24.entity.calculations.TariffRestBox;
import org.dbs24.entity.AbstractRetailLoanContract;
import org.dbs24.entity.tariff.TariffCalcSum;
import org.dbs24.entity.tariff.TariffRate;
import org.dbs24.references.serv.TariffServCashBack;
import java.time.LocalDate;
import java.util.Collection;

@TariffKindId(
        //        tariff_serv_class = TariffServCashBack.class,
        tariff_serv_id = TariffConst.TS_CASHBACK,
        tariff_kind_id = TariffConst.TK_CASHBACK,
        tariff_kind_name = "КэшБэк (БИБ)",
        tariff_scheme_id = TariffConst.SCH_30_365)
public class TariffKindCashBack4BIB extends TariffKindService<TariffServCashBack, AbstractRetailLoanContract, TariffRate_1, TariffRestBox> {

    @Override
    public Collection<TariffCalcSum> calculateTariff(
            final AbstractRetailLoanContract entity,
            final TariffRate tariffRate,
            final LocalDate D1, final LocalDate D2) {
        return null;
    }

}
