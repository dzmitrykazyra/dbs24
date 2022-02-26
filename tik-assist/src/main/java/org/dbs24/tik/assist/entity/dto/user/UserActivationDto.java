/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class UserActivationDto {

    private String activationKey;

    public static UserActivationDto toDto(String activationKey) {

        return StmtProcessor.create(
                UserActivationDto.class,
                userActivationDto -> userActivationDto.setActivationKey(activationKey)
        );
    }
}
