/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "monitoring")
@Validated
public class MonitoringProperties extends AbstractConfigProperties {

    protected Boolean enabled;
    protected String address;
    protected Integer port;
    protected String route;
    protected Integer blockDelay;
    private String uid = "smuser";
    private String pwd = "smpwd";

}
