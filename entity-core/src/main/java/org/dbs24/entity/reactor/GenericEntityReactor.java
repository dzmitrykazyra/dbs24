/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.reactor;

import java.util.Collection;
import lombok.Data;
import org.dbs24.entity.core.AbstractActionEntity;
import org.springframework.stereotype.Service;


@Data
@Service
public class GenericEntityReactor<T extends AbstractActionEntity> extends AbstractEntityReactor<Collection<T>> {
    
}
