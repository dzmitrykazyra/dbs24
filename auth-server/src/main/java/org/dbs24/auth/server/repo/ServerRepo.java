/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.repo;

import org.dbs24.auth.server.entity.Server;
import org.dbs24.auth.server.entity.ServerStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ServerRepo extends ApplicationJpaRepository<Server, Integer> {

    Optional<Server> findByServerAddressAndPid(String serverAddress, Long pid);

    Collection<Server> findByServerStatus(ServerStatus serverStatus);

}
