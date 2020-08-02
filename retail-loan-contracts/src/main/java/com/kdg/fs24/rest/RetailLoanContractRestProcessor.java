/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.rest;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.rest.api.SpringRestProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.kdg.fs24.consts.RetailLoanContractConst;
import java.time.LocalDateTime;
import com.kdg.fs24.service.RetailLoanContractActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kdg.fs24.application.core.log.LogService;
import org.springframework.http.HttpStatus;
import java.util.concurrent.atomic.AtomicLong;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@RestController
@RequestMapping(LogService.PATH_REST)
@CrossOrigin
@Deprecated
//@Data
public class RetailLoanContractRestProcessor extends SpringRestProcessor {

    @Autowired
    private RetailLoanContractActionsService retailLoanContractActionsService;

    @Override
    protected void initRestService() {

    }

    
    //==========================================================================
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/sayhello")
    @ResponseStatus(HttpStatus.OK)
    public String sayHi() {
        return "Hello World!";
    }

//    @GetMapping("/cancel3")
//    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
//    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return new Greeting(counter.incrementAndGet(), String.format(template, name));
//    }
    //==========================================================================
    @GetMapping("/testgreeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("%s: %s",
                this.getClass().getCanonicalName(),
                name);
    }

    //==========================================================================
    @PostMapping(RetailLoanContractConst.REST_RETAIL_LOAN_REPAYMENT)
    //consumes = "application/json; charset=utf-8;")
    //consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String receivePayments(@NotNull final String pingMessage) {

        final String outMsg = String.format("connection is successfull \n {'%s', %s, %s} ",
                pingMessage,
                this.getClass().getCanonicalName(),
                LocalDateTime.now());

        LogService.LogInfo(this.getClass(), () -> outMsg);

        retailLoanContractActionsService.hashCode();

        return outMsg;

    }
    //==========================================================================

}
