/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.starter.service;

import okhttp3.OkHttpClient;

@FunctionalInterface
public interface TaskProxyRunner {

    void runTask(OkHttpClient okHttpClient) throws Throwable;
}
