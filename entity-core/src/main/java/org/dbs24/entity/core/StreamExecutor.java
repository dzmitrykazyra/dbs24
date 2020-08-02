/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core;

import org.dbs24.application.core.exception.api.InternalAppException;
import java.sql.SQLException;

/**
 *
 * @author kazyra_d
 */
@FunctionalInterface
public interface StreamExecutor {

    void execute() throws InternalAppException, SQLException;
}
