/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.email;

import lombok.Data;

@Data
public class Message {

    public String code;
    public String bodyTruncated;
    public String emailFrom;
    public String nameFrom;
    public String thread;
    public String title;
    public long timestamp;
}
