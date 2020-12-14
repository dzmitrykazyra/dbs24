/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;
import static org.dbs24.consts.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
public class LiasesConst {

    public static final String LIASES_TEMLATE_PKG = "org.dbs24.entity.liases.templates";
    
    public static final int LTI_INITIAL_LIASES = 1;
    public static final int LTI_CURRENT_LIASES = 2; // Текущие обязательства (Обязательства по возврату базисного актива)
    public static final int LTI_ACCESS_LIASES = 3;
    public static final int LTI_PAYMENT_LIASES = 4;
    public static final int LTI_RESERVE_LIASES = 5;

    public static final int LGI_1 = 1;
    public static final int LGI_2 = 2;
    public static final int LGI_3 = 3;
    public static final int LGI_4 = 4;
    public static final int LGI_5 = 5;
    public static final int LGI_6 = 6;
    public static final int LGI_7 = 7;
    public static final int LGI_8 = 8;
    public static final int LGI_9 = 9;
    public static final int LGI_10 = 10;
    public static final int LGI_11 = 11;

    public static final int LKI_RETURN_MAIN_DEBT = 1; // "Требования по возврату базисного актива"
    public static final int LKI_2 = 2;
    public static final int LKI_PERC_PAY_REQUIREMENT = 3; // Требования по уплате процентов
    public static final int LKI_4 = 4;
    public static final int LKI_5 = 5;
    public static final int LKI_6 = 6;
    public static final int LKI_7 = 7;
    public static final int LKI_8 = 8;
    public static final int LKI_9 = 9;
    public static final int LKI_10 = 10;

    public static final int FOC_0 = 0;
    public static final int FOC_1 = 1;
    public static final int FOC_MAIN_PLACEMENT = 2;  // Размещение средств
    public static final int FOC_MAIN_ACCRETION = 4;  // Начисление процентных и комиссионных доходов
    public static final int FOC_RETURN_PLACEMENT = 9;  // Возврат размещенных средств

    public static final int LAT_GET_PRIMARY_LIASES = 1;
    public static final int LAT_2 = 2;
    public static final int LAT_3 = 3;
    public static final int LAT_4 = 4;
    public static final int LAT_5 = 5;
    public static final int LAT_6 = 6;
    public static final int LAT_INCREMENT_LIASES = 7; // Увеличение суммы обязательства
    public static final int LAT_8 = 8;
    public static final int LAT_9 = 9;

    public static final int LDS_NORMAL_DEBTS = 1;
    public static final int LDS_2 = 2;
    public static final int LDS_3 = 3;
    public static final int LDS_4 = 4;
    public static final int LDS_5 = 5;
    public static final int LDS_6 = 6;
    public static final int LDS_7 = 7;
    public static final int LDS_8 = 8;
    public static final int LDS_9 = 9;
    public static final int LDS_10 = 10;

    public static final int LBAT_MONEYS = 1; // Денежные средства
    public static final int LBAT_2 = 2;
    public static final int LBAT_3 = 3;
    public static final int LBAT_4 = 4;
    public static final int LBAT_5 = 5;
    public static final int LBAT_6 = 6;
    public static final int LBAT_7 = 7;
    public static final int LBAT_8 = 8;
    public static final int LBAT_9 = 9;
    public static final int LBAT_10 = 10;
    public static final int LBAT_11 = 11;
    public static final int LBAT_12 = 12;
    public static final int LBAT_13 = 13;
    public static final int LBAT_14 = 14;

    public static final Boolean THROW_EXC_WHEN_NOT_FOUND = Boolean.TRUE;
    public static final Boolean DONT_THROW_EXC = Boolean.FALSE;
}
