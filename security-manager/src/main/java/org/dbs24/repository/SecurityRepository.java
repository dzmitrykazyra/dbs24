/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Козыро Дмитрий
 */
@Transactional(readOnly = true)
@Repository
public class SecurityRepository {

//    @Autowired
//    ApplicationRoleRepository applicationRoleRepository;
//
//    @Autowired
//    ApplicationUserRepository applicationUserRepository;

}
