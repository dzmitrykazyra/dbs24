/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.Data;


@Data
public abstract class ActionResult {
    
    private Byte code;
    private String message;
    private String note;
    
}
