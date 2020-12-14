/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import static org.dbs24.consts.SysConst.ENTITY_PACKAGE;
import org.dbs24.entity.core.api.EntityClassesPackages;
import org.springframework.stereotype.Service;

@Data
@Service
@EntityClassesPackages(pkgList = {ENTITY_PACKAGE})
public class ScheduleBuildersActionsService extends AbstractActionExecutionService {

}
