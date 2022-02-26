/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class WarnInfo {
    
    private WarnType warnType;
    private String warnMsg;
    
    public static WarnInfo create(WarnType warnType, String warnMsg) {
        return StmtProcessor.create(WarnInfo.class, ei -> {
            ei.setWarnMsg(warnMsg);
            ei.setWarnType(warnType);
        });
    }
}
