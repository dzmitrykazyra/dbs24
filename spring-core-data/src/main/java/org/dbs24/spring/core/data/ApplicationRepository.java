/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.data;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.dbs24.spring.core.api.ApplicationBean;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository<T extends Object, ID extends Object> 
        extends CrudRepository<T, ID>, ApplicationBean {
}
