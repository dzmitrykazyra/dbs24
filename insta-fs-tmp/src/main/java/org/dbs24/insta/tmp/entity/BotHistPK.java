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
public class BotHistPK implements Serializable {

    private Integer botId;
    private LocalDateTime actualDate;

}
