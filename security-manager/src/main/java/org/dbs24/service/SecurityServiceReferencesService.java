/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.entity.core.api.ReferencesClassesPackages;
import org.dbs24.spring.core.data.AbstractReferencesService;
import org.springframework.stereotype.Service;
import static org.dbs24.consts.SysConst.*;

@Service
@ReferencesClassesPackages(pkgList = {SECURITY_PACKAGE})
public class SecurityServiceReferencesService extends AbstractReferencesService {

}
