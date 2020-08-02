/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff;

import java.util.Collection;

/**
 *
 * @author kazyra_d
 */
public interface TarrifGenerator {

    Collection<TariffCalcSum> execute();
}
