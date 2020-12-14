/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.serv;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.consts.TariffConst;
import org.dbs24.references.tariffs.serv.TariffServ;
import org.dbs24.references.tariffs.serv.TariffServId;

@TariffServId(serv_id = TariffConst.TS_MAIN_PERCENTS,
        group_id = TariffConst.TG_LOANS,
        serv_name = "Проценты по счету основного долга")
public class TariffServMainPerc extends TariffServ {

}
