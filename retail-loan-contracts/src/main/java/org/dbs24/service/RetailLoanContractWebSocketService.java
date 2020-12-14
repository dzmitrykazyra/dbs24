/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.dbs24.entity.RetailLoanContract;
import org.dbs24.stmt.StmtProcessor;

@Data
@Service
@Log4j2
public class RetailLoanContractWebSocketService extends AbstractWebSocketService {

    public String object2Json(RetailLoanContract retailLoanContract) {
        return StmtProcessor.create(()-> this.getObjectMapper().writeValueAsString(retailLoanContract));
    }

    public <T> T json2Object(String json, Class<T> clazz) {
        return (T) getObjectMapper().convertValue(json, clazz);
    }

}
