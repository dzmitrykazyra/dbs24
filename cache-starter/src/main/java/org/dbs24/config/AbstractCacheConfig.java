/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;


public abstract class AbstractCacheConfig extends MainApplicationConfig {

//    @Bean
//    public CacheManager cacheManager() {
//        return new EhCacheCacheManager();
//    }
//
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheCacheManager() {
//        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
//        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        cmfb.setShared(true);
//        return cmfb;
//    }

}
