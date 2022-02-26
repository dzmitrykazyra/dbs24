/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.rest.importer;

import lombok.Getter;
import java.util.Collection;

@Getter
public class Countries {
    private String status;
    private Collection<Country> data;    
}
