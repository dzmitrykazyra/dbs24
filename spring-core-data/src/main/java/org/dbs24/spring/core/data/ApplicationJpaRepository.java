/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.data;

import org.dbs24.spring.core.api.ApplicationBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Козыро Дмитрий
 */
//@Repository
@NoRepositoryBean
@Transactional(readOnly = true)
public interface ApplicationJpaRepository<T, ID>
        extends JpaRepository<T, ID> { //, ApplicationBean {

}
