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

@TariffServId(serv_id = TariffConst.TS_CASHBACK,
        group_id = TariffConst.TG_LOANS,
        serv_name = "Возврат средств (cashback)",
        client_pay = false
)
public class TariffServCashBack extends TariffServ {
    
}
