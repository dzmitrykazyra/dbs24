/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.component.CommonRest;
import org.dbs24.rest.api.SystemInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import static org.dbs24.consts.SecurityConst.SYSTEM_INFO_CLASS;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class TktAssistCommonRest extends CommonRest {

    final StopWatcher stopWatcher = StopWatcher.create("Server uptime");

    public TktAssistCommonRest(GenericApplicationContext genericApplicationContext) {
        super(genericApplicationContext);
    }
   //==========================================================================
    @Override
    protected SystemInfo buildLiveNessRecord() {
        return StmtProcessor.create(SYSTEM_INFO_CLASS,
                object -> object.setSysInfo(String.format("%s, tasks in progress: %d, mailboxes available: %d, account(s) created: %d/%d, total accounts avaiable: %d ",
                        stopWatcher.getStringExecutionTime("is"),
                        0,
                        0,
                        0,
                        0,
                        0))
        );
    }
}
