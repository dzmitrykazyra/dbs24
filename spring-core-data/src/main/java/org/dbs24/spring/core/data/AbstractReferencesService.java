/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.data;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import java.util.Collection;
import lombok.Data;
import org.dbs24.spring.core.api.AbstractApplicationService;

@Data
public abstract class AbstractReferencesService extends AbstractApplicationService {
    
    private Collection<ApplicationJpaRepository> refRepositories;
}
