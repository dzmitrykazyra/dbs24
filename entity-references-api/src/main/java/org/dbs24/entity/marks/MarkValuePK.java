/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class MarkValuePK implements Serializable {

    private Integer markId;
    private Integer markValueId;
}
