/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import lombok.EqualsAndHashCode;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.dbs24.spring.core.api.ApplicationConfiguration;

@EqualsAndHashCode(callSuper = false)
public abstract class AbstractApplicationConfiguration extends AbstractApplicationBean implements ApplicationConfiguration {

}
