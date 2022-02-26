/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import org.dbs24.insta.tmp.entity.Task;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;


public interface TaskRepo extends ApplicationJpaRepository<Task, Long> {

    @Query(value = "select * from tasks where 1 = :AT OR task_result_id = :TR order by task_id desc LIMIT 500 ", nativeQuery = true)
    Collection<Task> findTasks( @Param("AT") Integer allTasks, @Param("TR") Integer taskResultId);

}
