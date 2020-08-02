/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

import org.dbs24.references.tariffs.serv.TariffServ;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public final class TariffPlan2ServPK implements Serializable {

    private AbstractTariffPlan tariffPlan;
    private TariffServ tariffServ;
}
