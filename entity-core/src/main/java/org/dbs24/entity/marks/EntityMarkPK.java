/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks;

import org.dbs24.entity.core.api.Action;
import org.dbs24.entity.core.api.ActionEntity;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class EntityMarkPK implements Serializable {
    private ActionEntity entity;
    private Action action;
}
