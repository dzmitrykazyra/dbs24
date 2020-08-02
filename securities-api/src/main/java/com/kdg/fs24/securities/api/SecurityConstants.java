/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.securities.api;

// коды действий над сущносятми связанными с ЦБ
public class SecurityConstants {

    public static final Integer MODIFY_BOND_CONTRACT = 102110001; // редактировать облигация собственной эмиссии
    public static final Integer MODIFY_NATIONAL_BOND_CONTRACT = 102120001;// редактировать облигация в нацвалюте
    public static final Integer MODIFY_FOREIGN_BOND_CONTRACT = 102130001; // редактировать облигация в инвалюте

    public static final Integer BOND_OF_OWN_ISSUE = 102110; // облигация собственной эмиссии
    public static final Integer NATIONAL_BOND = 102120;// облигация в нацвалюте
    public static final Integer FOREIGN_BOND = 102130; // облигация в инвалюте

}
