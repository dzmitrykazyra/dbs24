/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.UserContract;
import org.dbs24.stmt.StmtProcessor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
public class UserContractInfo {

    @EqualsAndHashCode.Include
    private Integer contractId;
    private Long actualDate;
    private Integer userId;
    private Integer contractTypeId;
    private Byte contractStatusId;
    private Long beginDate;
    private Long endDate;
    private Long cancelDate;
    private Integer subscriptionsAmount;
    private Integer modifyReasonId;
    private String editNote;

    public void assign(UserContract userContract) {
        contractId = userContract.getContractId();
        actualDate = NLS.localDateTime2long(userContract.getActualDate());
        beginDate = NLS.localDateTime2long(userContract.getBeginDate());
        endDate = NLS.localDateTime2long(userContract.getEndDate());
        cancelDate = NLS.localDateTime2long(userContract.getCancelDate());
        contractStatusId = userContract.getContractStatus().getContractStatusId();
        contractTypeId = userContract.getContractType().getContractTypeId();
        subscriptionsAmount = userContract.getSubscriptionsAmount();
        userId = userContract.getUser().getUserId();
        StmtProcessor.ifNotNull(userContract.getModifyReason(), mr -> modifyReasonId = mr.getModifyReasonId());
        editNote = userContract.getEditNote();
    }
}
