/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.bondschedule.builders;

import java.util.Collection;
import org.dbs24.entity.bondschedule.PmtSchedule;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.dbs24.entity.AbstractEntityContract;

/**
 *
 * @author kdg
 */
public abstract class BondScheduleBuilder<T extends AbstractEntityContract> extends AbstractApplicationBean {

    public abstract Collection<PmtSchedule> createBondschedules(T entityContract);
}
