/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;
import com.kdg.fs24.spring.core.bean.AbstractApplicationBean;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class WebClientMgmt extends AbstractApplicationBean {

    private WebClient webClient = WebClient.create();

}
