/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff.api;

import com.kdg.fs24.references.tariffs.kind.TariffKindOld;

/**
 *
 * @author Козыро Дмитрий
 */
public interface TariffKindProcessor {

     void processTariffKind(TariffKindOld tariffKind);
}
