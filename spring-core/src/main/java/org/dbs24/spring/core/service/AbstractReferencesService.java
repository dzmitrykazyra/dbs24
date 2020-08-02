/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.service;

import java.util.Collection;
import org.dbs24.spring.core.api.ApplicationJpaRepository;
import lombok.Data;
import org.dbs24.spring.core.api.ApplicationService;
/**
 *
 * @author Козыро Дмитрий
 */

@Data
public abstract class AbstractReferencesService implements ApplicationService {
    
    private Collection<ApplicationJpaRepository> refRepositories;
}
