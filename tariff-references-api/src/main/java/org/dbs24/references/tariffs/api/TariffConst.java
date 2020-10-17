/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.api;

import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.references.tariffs.kind.TariffKindAbstract;
import org.dbs24.references.tariffs.kind.TariffKindId;
import org.dbs24.references.tariffs.serv.TariffServId;

/**
 *
 * @author Козыро Дмитрий
 */
public class TariffConst {
    
    //public static Boolean tariffDebug = BOOLEAN_FALSE;
    

    public static final Class<TariffServId> TARIFF_SERVID_CLASS = TariffServId.class;
    public static final Class<TariffKindAbstract> TARIFF_KIND_ABSTRACT_CLASS = TariffKindAbstract.class;
    public static final Class<TariffKindId> TARIFF_KINDID_CLASS = TariffKindId.class;
    
    public static final int TG_LOANS = 101;  // Предоставление средств в форме кредита

    public static final int ENTITY_TARIFF_PLAN = 1001;  // Тарифный план 

    public static final int EK_TP_FOR_RETAIL_LOAN_CONTRACT = 10010001;  // Тарифный план для кредитного договора с ФЛ
    public static final int EK_TP_FOR_RETAIL_DEPO_CONTRACT = 10010002;  // Тарифный план для депозитного договора с ФЛ

    public static final int ACT_MODIFY_TARIFF_PLAN = 100110001;
    public static final int ACT_AUTHORIZE_TARIFF_PLAN = 100110002;
    public static final int ACT_CANCEL_TARIFF_PLAN = 100110010;
    public static final int ACT_CLOSE_TARIFF_PLAN = 100110011;
    public static final int ACT_REOPEN_TARIFF_PLAN = 100110012;

    //==========================================================================
    public static final int TS_MAIN_PERCENTS = 10001;  // Проценты по счету учета договора
    public static final int TS_MAIN_FEE = 10002;  // Комиссия за ведение счета
    public static final int TS_PERC_OVERDUE = 10003;  // Проценты посчету просроченного долга
    public static final int TS_SOME_SERV = 10004;  // Ещё какая-то комиссия
    public static final int TS_CASHBACK = 20001;  // Кэшбэк по операциям покупки

    public static final int TK_CURRENT_RESTS = 100011;  // По текущим остаткам
    public static final int TK_DEBTS_TURNS = 100012;  // По дебетовому обороту
    public static final int TK_CREDIT_TURNS = 100013;  // По кредитовому обороту
    
    public static final int TK_CASHBACK = 100020;  // кэшбэк
    
    public static final int TS_SOME_SERV_001 = 10004001;  // По виртуальным остаткам
    
    public static final int TK_CACHBACK_BY_TRANSACTION = 20001001;  // Кэшбэк от суммы операции

    public static final boolean CLIENT_PAY = true;  // Клиент платит
    public static final boolean BANK_PAY = false;  // Банк платит

    public static final int SCH_30_360 = 1;  // 30/360
    public static final int SCH_30_365 = 2;  // 30/365    
    public static final int SCH_FACT_FACT = 3;  // FACT/FACT    

    public static final int TGR_BY = 1;  // Нормативные величины и ставки РБ
    public static final int TGR_RU = 2;  // Нормативные величины и ставки РФ
    public static final int TGR_LIBOR = 100;  // Ставки Federal Funds Target Rate (USA)
    public static final int TGR_EURIBOR = 110;  // Cтавки EUR Libor
    public static final int TGR_BIS = 200;  // Ставки Банка Международных Расчетов (BIS)
    public static final int TGR_MVF = 300;  // Cтавки МВФ

    public static final int TGR_BY_BMP = 10001;  // Размер Бюджета прожиточного минимума в среднем на душу населения
    public static final int TGR_BY_REFIN = 10002;  // Ставка рефинансирования НБРБ
    public static final int TGR_BY_BV = 10003;  // Размер Базовой величины
    public static final int TGR_BY_MZP = 10004;  // Месячная Минимальная заработная плата
    //==========================================================================

}
