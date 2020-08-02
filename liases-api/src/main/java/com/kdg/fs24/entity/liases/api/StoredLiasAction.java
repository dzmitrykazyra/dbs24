/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.liases.api;

/**
 *
 * @author kazyra_d
 */


import java.time.LocalDateTime;

public interface StoredLiasAction { //extends LiasAction {

    int getLias_action_id();

    int getLias_id();

    LocalDateTime getServer_date();

    int getStatus();
    
    void store(Integer lias_id);

}
