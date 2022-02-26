/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api.dto;

import lombok.Data;

import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK_STR;


@Data
public abstract class OperationResult {

    private Integer answerCode = OC_OK;
    private String note = OC_OK_STR;
}
