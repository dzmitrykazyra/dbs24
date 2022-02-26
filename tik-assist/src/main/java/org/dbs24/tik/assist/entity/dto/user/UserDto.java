/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.user;

import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.User;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private String email;
    private String pass;

    public static User defaultUserFromDto(UserDto userDto) {

        return User.builder()
                .actualDate(LocalDateTime.now())
                .email(userDto.getEmail())
                .build();
    }
}
