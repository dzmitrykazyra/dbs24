/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.debts;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "liasDebtRests")
@IdClass(LiasDebtRestPK.class)
public class LiasDebtRest extends ObjectRoot implements PersistenceEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "debt_id")
    private LiasDebt liasDebt;
    @Id
    @Column(name = "rest_type")
    private Integer restType;
    @Id
    @Column(name = "rest_date")
    private LocalDate restDate;
    private BigDecimal rest;

    //==========================================================================
    public void incRest(final BigDecimal rest) {
        this.rest = this.rest.add(rest);
    }

}
