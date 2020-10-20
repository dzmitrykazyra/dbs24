/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.application.application;

import org.dbs24.references.api.ReferenceRec;
import java.util.Map;
import org.dbs24.references.api.AbstractRefRecord;

/**
 *
 * @author kazyra_d
 */
public class Application extends AbstractRefRecord implements ReferenceRec {

    private Integer app_id;
    private String app_code;
    private String app_name;
    private String app_url;

    public Application() {
        super();
    }

    public Integer getApp_id() {
        return app_id;
    }

    public Application setApp_id(final Integer app_id) {
        this.app_id = app_id;
        return this;
    }

    public String getApp_code() {
        return app_code;
    }

    public Application setApp_code(final String app_code) {
        this.app_code = app_code;
        return this;
    }

    public String getApp_name() {
        return app_name;
    }

    public Application setApp_name(final String app_name) {
        this.app_name = app_name;
        return this;
    }

    public String getApp_url() {
        return app_url;
    }

    public Application setApp_url(final String app_url) {
        this.app_url = app_url;
        return this;
    }
}
