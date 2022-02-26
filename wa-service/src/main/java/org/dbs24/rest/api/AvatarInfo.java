/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AvatarInfo {

    private Integer subscriptionId;
    private Long avatarId;
    private boolean isCustomAvatar;
    private byte[] avatarImg;
}
