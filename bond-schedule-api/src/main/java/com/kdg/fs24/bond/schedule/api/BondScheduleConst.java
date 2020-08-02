/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.bond.schedule.api;

/**
 *
 * @author Козыро Дмитрий
 */
public final class BondScheduleConst {

    public static final int BONDSCHEDULE = 2000; // график платежей

    public static final int EK_BONDSCHEDULE_MAIN_DEBT = 20001; // График погашения ОД
    public static final int EK_BONDSCHEDULE_PERC = 20002; // График погашения %
    public static final int EK_BONDSCHEDULE_COMM = 20003; // График погашения комисов

    public static final int BS_ALG_BYREST = 1; // алгоритм расчета графика 1
    public static final int BS_ALG_ANNUITET = 2; // алгоритм расчета графика 2
    
    public static final int ACT_SAVE_BONDSCHEDULE = 1000001; // сохранить график алатежей
    
    public static final int ES_DEFAULT_STATUS = 0; // статус графика по умолчанию

}
