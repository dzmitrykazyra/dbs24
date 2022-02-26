/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.ad.server.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.service.AbstractReferencesService;
import org.springframework.stereotype.Component;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class RefsService extends AbstractReferencesService {

//    @Value("${refs.synchronize:true}")
//    private Boolean needSynchronize;
//

}
