/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccountHistPK implements Serializable {

    private Integer accountId;
    private LocalDateTime actualDate;

}
