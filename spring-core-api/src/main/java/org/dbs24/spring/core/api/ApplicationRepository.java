/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Козыро Дмитрий
 */
public interface ApplicationRepository<T extends Object, ID extends Object> 
        extends CrudRepository<T, ID>, ApplicationBean {

}
