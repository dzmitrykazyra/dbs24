/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class BatchActionPK implements Serializable {

    private Integer action;
    private Integer batch;

}
