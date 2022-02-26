/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.bot.response;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class AvailableBotsBySecUserIdResponse {

    private String secUserId;
    private Integer botsAmount;

    public static AvailableBotsBySecUserIdResponse toDto(String secUserId, Integer botsAmount) {

        return StmtProcessor.create(
                AvailableBotsBySecUserIdResponse.class,
                availableBotsBySecUserIdResponse -> {
                    availableBotsBySecUserIdResponse.setSecUserId(secUserId);
                    availableBotsBySecUserIdResponse.setBotsAmount(botsAmount);
                }
        );
    }
}
