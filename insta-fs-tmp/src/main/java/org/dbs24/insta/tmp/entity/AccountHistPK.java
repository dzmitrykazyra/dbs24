/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Deprecated
public class AccountHistPK implements Serializable {

    private Long accountId;
    private LocalDateTime actualDate;
    
}
