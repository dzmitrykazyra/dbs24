/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.spring.core.service;

import java.util.Collection;
import com.kdg.fs24.spring.core.api.ApplicationJpaRepository;
import lombok.Data;
import com.kdg.fs24.spring.core.api.ApplicationService;
/**
 *
 * @author Козыро Дмитрий
 */

@Data
public abstract class AbstractReferencesService implements ApplicationService {
    
    private Collection<ApplicationJpaRepository> refRepositories;
}
