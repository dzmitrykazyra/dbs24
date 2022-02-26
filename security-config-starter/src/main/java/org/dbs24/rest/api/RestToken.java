/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import java.util.Collection;
import java.time.LocalDateTime;

public final class RestToken {

    String id;
    String user;
    Collection<String> roles;
    LocalDateTime expiry;

}
