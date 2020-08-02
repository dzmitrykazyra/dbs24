/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.kind;

import java.util.Collection;

/**
 *
 * @author Козыро Дмитрий
 */
public interface TariffBox {

    Collection<TariffCalcSumExtended> getTariffSums();
    
    void printCalculations(TariffKind tariffKind);
}
