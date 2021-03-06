/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;
import org.dbs24.spring.core.api.AbstractApplicationBean;

@Data
public class WebClientMgmt extends AbstractApplicationBean {

    final WebClient webClient;

    public WebClientMgmt() {
        this.webClient = WebClient.create();
    }
}
