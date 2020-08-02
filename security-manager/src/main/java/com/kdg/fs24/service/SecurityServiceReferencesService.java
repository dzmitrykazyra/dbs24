/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.entity.core.api.ReferencesClassesPackages;
import com.kdg.fs24.spring.core.service.AbstractReferencesService;
import lombok.Data;
import org.springframework.stereotype.Service;
import com.kdg.fs24.application.core.sysconst.SysConst;

/**
 *
 * @author Козыро Дмитрий
 */

@Data
@Service
@ReferencesClassesPackages(pkgList = {SysConst.SECURITY_PACKAGE})
public class SecurityServiceReferencesService extends AbstractReferencesService {

}
