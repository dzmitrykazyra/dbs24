/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.advice;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Log4j2
public abstract class AbstractExceptionController extends AbstractApplicationService {

    @ExceptionHandler(Throwable.class)
    public String processException(Throwable throwable) {

        log.error(throwable.getMessage());

        return throwable.getMessage();
    }

}
