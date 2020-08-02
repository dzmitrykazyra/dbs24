/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.spring.core.repository;

import com.kdg.fs24.spring.core.bean.AbstractApplicationBean;
import java.util.Collection;
import com.kdg.fs24.spring.core.api.ApplicationJpaRepository;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Deprecated
public final class JpaRepositoriesCoolection { //extends AbstractApplicationBean {

    private Collection<ApplicationJpaRepository> repositores;
}
