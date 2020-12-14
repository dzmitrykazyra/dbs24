/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import static org.dbs24.consts.RetailLoanContractConst.*;
import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.EntityConst.*;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.entity.status.EntityStatusId;
import org.dbs24.references.loan.api.LoanSource;
import org.dbs24.references.bond.schedule.api.PmtScheduleAlg;
import org.dbs24.references.bond.schedule.api.PmtScheduleTerm;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "rlc_loanContracts")
@PrimaryKeyJoinColumn(name = "contract_id", referencedColumnName = "contract_id")
@EntityTypeId(entity_type_id = LOAN2INDIVIDUAL,
        entity_type_name = "Кредиты физическим лицам")
@EntityKindId(entity_kind_id = LOAN2INDIVIDUAL_CARD,
        entity_type_id = LOAN2INDIVIDUAL,
        entity_kind_name = "Кредит ФЛ на карточку")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = LOAN2INDIVIDUAL,
                    entity_status_id = ES_ACTUAL,
                    entity_status_name = "Действующая сделка"),
            @EntityStatusId(
                    entity_type_id = LOAN2INDIVIDUAL,
                    entity_status_id = ES_CLOSED,
                    entity_status_name = "Закрытая сделка"),
            @EntityStatusId(
                    entity_type_id = LOAN2INDIVIDUAL,
                    entity_status_id = ES_CANCELLED,
                    entity_status_name = "Аннулированная сделка")
        })
@DefaultEntityStatus(entity_status = ES_ACTUAL)
@ActionClassesPackages(pkgList = {ACTIONS_PACKAGE})
public class RetailLoanContract extends AbstractRetailLoanContract {

    @ManyToOne
    @JoinColumn(name = "loan_source_id", referencedColumnName = "loan_source_id")
    private LoanSource loanSource;
    @ManyToOne
    @JoinColumn(name = "schedule_alg_id", referencedColumnName = "schedule_alg_id")
    private PmtScheduleAlg pmtScheduleAlg;
    @ManyToOne
    @JoinColumn(name = "pmt_term_id", referencedColumnName = "pmt_term_id")
    private PmtScheduleTerm pmtScheduleTerm;
}
