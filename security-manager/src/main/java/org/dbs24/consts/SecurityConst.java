/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import static org.dbs24.consts.RestHttpConsts.*;
import org.dbs24.rest.api.*;

public final class SecurityConst {

    public static final int FS24_USER = 100;                    // пользователь системы
    public static final int FS24_USER_BASE = 100001;                    // базовый пользователь системы
    public static final int FS24_ROLE = 101;                    // роль системы
    public static final int FS24_ROLE_BASE = 101001;                    // базовый роль системы    
    public static final int ACT_CREATE_OR_MODIFY_USER = 1001001; // регистрация пользователя
    public static final int ACT_CREATE_OR_MODIFY_ROLE = 1001002; // регистрация роли

    //==========================================================================
    public static final Class<SystemInfo> SYSTEM_INFO_CLASS = SystemInfo.class;
    public static final Class<IamReady> I_AM_READY_CLASS = IamReady.class;

}
