/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

/**
 *
 * @author kazyra_d
 */
import java.time.LocalDate;
import lombok.Data;

@Data
public final class TariffAccretionHisory {

    private LocalDate accretion_date;
    private Integer tariff_serv_id;
    private Integer tariff_kind_id;
    private Long contract_id;
    private Long lias_action_id;    
}
