/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.jpa.spec;


import org.springframework.data.jpa.domain.Specification;
import org.dbs24.entity.AppUser;

@FunctionalInterface
public interface AppUsersFilter {
    Specification<AppUser> setFilter(Integer idFrom, Integer idTo);
}
