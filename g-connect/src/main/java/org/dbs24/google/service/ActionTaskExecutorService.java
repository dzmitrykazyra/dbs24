package org.dbs24.google.service;

import com.github.yeriomin.playstoreapi.exception.account.ApiBuilderException;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.entity.dto.proxy.Proxy;

import java.io.IOException;

public interface ActionTaskExecutorService {

    OrderActionResult install(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException;

    OrderActionResult rateComment(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException;

    OrderActionResult review(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException;

    OrderActionResult flagContent(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException;

    OrderActionResult applicationActivity(ExecOrderAction taskToExecute) throws Exception;

    OrderActionResult simulateSearch(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException;
}
