/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.mail;

import org.dbs24.application.core.log.LogRecord;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class MailRecord extends LogRecord {

    private String header;
    private String body;
    private String address;

}
