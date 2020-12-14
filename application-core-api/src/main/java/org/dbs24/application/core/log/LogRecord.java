/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.log;

/**
 *
 * @author Козыро Дмитрий
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import static org.dbs24.consts.SysConst.*;

import java.time.LocalDateTime;

public class LogRecord implements LogRec {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime recDate;
    private String recClass;
    private String warPackage;
    private String ip_address;
    private String recDetails;

    public LogRecord() {
        super();
        ip_address = UNKNOWN;
    }

    public LogRecord( LocalDateTime recDate, String recDetails) {
        this();
        this.recDate = recDate;
        this.recDetails = recDetails;

    }
    //==========================================================================    

    public LocalDateTime getRecDate() {
        return recDate;
    }

    @Override
    public LogRecord setRecDate( LocalDateTime recDate) {
        this.recDate = recDate;
        return this;
    }

    public String getRecDetails() {
        return recDetails;
    }

    @Override
    public LogRecord setRecDetails( String recDetails) {
        this.recDetails = recDetails;
        return this;
    }

    public String getWarPackage() {
        return warPackage;
    }

    @Override
    public LogRecord setWarPackage( String warPackage) {
        this.warPackage = warPackage;
        return this;
    }

    public String getRecClass() {
        return recClass;
    }

    //@Override
    public LogRecord setRecClass( String recClass) {
        this.recClass = recClass;
        return this;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address( String ip_address) {
        this.ip_address = ip_address;
    }

}

