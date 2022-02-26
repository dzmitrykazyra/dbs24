/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.jpa.spec;


import org.dbs24.entity.AuthKey;
import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface AgentsFilter {

    Specification<AuthKey> setFilter(Integer idFrom, Integer idTo);
}
