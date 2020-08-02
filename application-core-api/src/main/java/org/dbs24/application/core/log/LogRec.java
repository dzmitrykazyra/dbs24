/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.log;

import java.time.LocalDateTime;
/**
 *
 * @author Козыро Дмитрий
 */
public interface LogRec {

    LogRec setRecDate(LocalDateTime recDate);

    LogRec setRecDetails(String recDetails);

    LogRec setWarPackage(String warPackage);

}
