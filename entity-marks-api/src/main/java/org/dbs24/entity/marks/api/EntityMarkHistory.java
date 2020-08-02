/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks.api;

import org.dbs24.entity.core.api.EntityHistoryAction;

/**
 *
 * @author kazyra_d
 */
public interface EntityMarkHistory extends EntityHistoryAction {

    int getMark_id();

    int getMark_value_id();

    Boolean getMark_direction();

    String getMark_name();

    String getMark_value_name();

}
