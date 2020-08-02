/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.liases.api;

/**
 *
 * @author kazyra_d
 */
import java.util.Collection;
import java.util.List;
import com.kdg.fs24.lias.opers.napi.OldLiasOper;

// перечень обязательств по договору
public interface ContractDebts<T> {

    //private List<LiasDebt>  debtsList;
    //void update(List<NewLiasOper> opers);
    void update(final Collection<OldLiasOper> opers);

    void store();

   // void load(Long entity_id, Integer counterparty_id);
    Collection<T> getContactDebts();

    //List<LiasDebt> getContactDebts();

   // void setContactDebts(List<LiasDebt> contactDebts);

}
