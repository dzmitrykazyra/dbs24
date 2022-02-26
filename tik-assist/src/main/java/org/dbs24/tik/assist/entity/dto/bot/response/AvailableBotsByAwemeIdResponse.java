/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.bot.response;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class AvailableBotsByAwemeIdResponse {

    private String awemeId;
    private Integer botsAmount;

    public static AvailableBotsByAwemeIdResponse toDto(String awemeId, Integer botsAmount) {

        return StmtProcessor.create(
                AvailableBotsByAwemeIdResponse.class,
                response -> {
                    response.setAwemeId(awemeId);
                    response.setBotsAmount(botsAmount);
                });
    }
}
