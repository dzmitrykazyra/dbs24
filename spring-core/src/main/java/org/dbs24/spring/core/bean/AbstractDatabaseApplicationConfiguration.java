/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.spring.core.api.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Value;

@Deprecated
public class AbstractDatabaseApplicationConfiguration implements ApplicationConfiguration {

    @Value("${driverClassName)")
    private String driverClassName;
    @Value("${url)")
    private String url;
    @Value("$(username)")
    private String username;
    @Value("${password)")
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
}
