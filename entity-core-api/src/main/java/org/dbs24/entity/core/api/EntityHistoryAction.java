/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core.api;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author kazyra_d
 */
public interface EntityHistoryAction {

    Long getAction_id();

    Long getEntity_id();

    int action_code();

    Long getUser_id();

    String getAction_name();

    String getApp_name();
    
    String getNotes();    

    LocalDateTime getExecute_date();

    String getErr_msg();

    String getAction_address();

    LocalTime getAction_duration();

}
