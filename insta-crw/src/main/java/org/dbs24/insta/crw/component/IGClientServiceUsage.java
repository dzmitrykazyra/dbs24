/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.crw.component;

import com.github.instagram4j.instagram4j.IGClient;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IGClientServiceUsage {

    private IGClient agent;
    private LocalDateTime lastUsed;
}
