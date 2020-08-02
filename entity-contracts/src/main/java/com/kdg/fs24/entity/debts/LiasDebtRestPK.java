/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.debts;


import java.io.Serializable;
import lombok.Data;
import java.time.LocalDate;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class LiasDebtRestPK implements Serializable {

    private LiasDebt liasDebt;
    private Integer restType;
    private LocalDate restDate;
}
