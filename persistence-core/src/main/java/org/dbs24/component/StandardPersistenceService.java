/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.persistence.service.name", havingValue = "standard", matchIfMissing = true)
public class StandardPersistenceService extends PersistenceService {


}
