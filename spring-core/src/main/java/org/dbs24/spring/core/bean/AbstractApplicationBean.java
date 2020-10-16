/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.bean;

import org.dbs24.spring.core.api.ApplicationBean;

/**
 *
 * @author Козыро Дмитрий
 */
public abstract class AbstractApplicationBean implements ApplicationBean {

    @Override
    public void initialize() {
    }

    @Override
    public void destroy() {
    }
}
