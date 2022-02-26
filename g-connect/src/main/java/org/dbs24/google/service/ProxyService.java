package org.dbs24.google.service;

import org.dbs24.google.entity.dto.proxy.Proxy;

import java.util.List;

public interface ProxyService {

    List<Proxy> bookProxies(Integer proxiesAmount);

    void unbookProxy(Proxy proxy);
}
