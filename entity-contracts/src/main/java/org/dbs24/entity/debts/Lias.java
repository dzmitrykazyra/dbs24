/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.debts;

import org.dbs24.application.core.service.funcs.CustomCollectionImpl;
import org.dbs24.entity.liases.exception.LiasRestIsNegative;
import org.dbs24.lias.opers.attrs.*;
import java.time.LocalDate;
import java.util.Collection;
//import org.dbs24.entity.liases.references.LiasesReferencesService;
import org.dbs24.entity.contracts.AbstractEntityContract;
import org.dbs24.application.core.api.ObjectRoot;
//import org.dbs24.services.api.ServiceLocator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.lias.opers.api.LiasOpersConst;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import org.dbs24.persistence.api.PersistenceEntity;
import javax.persistence.*;
import lombok.Data;
import org.dbs24.references.liases.finopercode.LiasFinOperCode;
import org.dbs24.references.liases.status.LiasOperStatus;
import org.dbs24.references.liases.actiontype.LiasActionType;
import org.dbs24.entity.document.Document;
import org.dbs24.service.LiasDocumentBuilders;
import org.dbs24.spring.core.api.ServiceLocator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Table(name = "liases")
public class Lias extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lias_id")
    @SequenceGenerator(name = "seq_lias_id", sequenceName = "seq_lias_id", allocationSize = 1)
    @Column(name = "lias_id")
    private Integer liasId;
    //--------------------------------------------------------------------------
    @OneToOne
    @JoinColumn(name = "debt_id", referencedColumnName = "debt_id")
    private LiasDebt liasDebt;
    //--------------------------------------------------------------------------
    @Column(name = "start_date")
    private LocalDate startDate;
    //--------------------------------------------------------------------------
    @Column(name = "allow_date")
    private LocalDate allowDate;
    //--------------------------------------------------------------------------
    @Column(name = "final_date")
    private LocalDate finalDate;
    //--------------------------------------------------------------------------
    @Column(name = "legal_date")
    private LocalDate legalDate;
    //--------------------------------------------------------------------------
    @Column(name = "server_date")
    private LocalDateTime serverDate;
    //--------------------------------------------------------------------------
    @Column(name = "inactive_date")
    private LocalDate inactiveDate;
    //--------------------------------------------------------------------------
    @Column(name = "is_canceled")
    private Boolean isCancelled;
    @org.hibernate.annotations.BatchSize(size = 100)
    @OneToMany(mappedBy = "lias", cascade = CascadeType.ALL)
    private Collection<LiasAction> liasActions = ServiceFuncs.<LiasAction>createCollection();

    @OneToMany(mappedBy = "lias", cascade = CascadeType.ALL)
    @org.hibernate.annotations.BatchSize(size = 100)
    private Collection<LiasRest> liasRests = ServiceFuncs.<LiasRest>createCollection();

    //==========================================================================
    // создание новой финоперации
    //==========================================================================
    public void createLiasOper(final BigDecimal liasSum,
            final LocalDate operDate,
            final Integer liasFinOperCode,
            final Integer liasTypeID,
            final Integer liasActionTypeId,
            final Document document) {

        NullSafe.create(this.liasActions)
                .execute(() -> {

                    final LiasAction newLiasAction = NullSafe.createObject(LiasAction.class);

                    newLiasAction.setLias(this);
                    newLiasAction.setLiasSum(liasSum);
                    newLiasAction.setLiasFinOperCode(LiasFinOperCode.findLiasFinOperCode(liasFinOperCode));
                    newLiasAction.setLiasOperStatus(LiasOperStatus.findLiasOperStatus(1));
                    newLiasAction.setLiasDate(operDate);
                    newLiasAction.setLiasActionType(LiasActionType.findLiasActionType(liasActionTypeId));

                    // документ на операции
                    newLiasAction.setDocument(document);

                    this.liasActions.add(newLiasAction);

                    this.createOrUpdateLiasRests(liasSum, operDate);
                }).throwException();

        // изменение остатков по задолженности
        //==========================================================================
    }

    //==========================================================================
    public void createOrUpdateLiasRests(final LiasFinanceOper liasFinanceOper) {
        this.createOrUpdateLiasRests(liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS),
                liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_DATE_CLASS));
    }

    //==========================================================================
    private void createOrUpdateLiasRests(final BigDecimal liasSum, final LocalDate operDate) {

        if (!ServiceFuncs.getCollectionElement(this.getLiasRests(),
                lr -> lr.getRestDate().equals(operDate)).isPresent()) {

            // предыдущий остаток
            final List<LiasRest> lastRest = this.getLiasRests()
                    .stream()
                    .unordered()
                    .filter(ldr -> ldr.getRestDate().isBefore(operDate))
                    .sorted((rd1, rd2) -> rd2.getRestDate().compareTo(rd1.getRestDate()))
                    .collect(Collectors.toList());

            final BigDecimal newRest;
            // остатки найдены
            if (!lastRest.isEmpty()) {
                newRest = lastRest.get(0).getRest();
            } else {
                newRest = BIGDECIMAL_ZERO;
            }

            final LiasRest liasRest = NullSafe.createObject(LiasRest.class);

            liasRest.setRest(newRest);
            liasRest.setRestType(INTEGER_ONE);
            liasRest.setRestDate(operDate);
            liasRest.setLias(this);

            // добавили новый остаток в коллекцию остатков
            this.getLiasRests().add(liasRest);

        }
        // Группа остатков для увеличения\уменьшения       
        this.getLiasRests()
                .stream()
                .unordered()
                .filter(ldr -> ldr.getRestDate().isAfter(operDate)
                || ldr.getRestDate().equals(operDate))
                .forEach(rest -> {
                    //увеличить/уменьшить остаток
                    rest.incRest(liasSum);
                });
    }

    //==========================================================================
//    @Deprecated
//    public void createLiasOper(final LiasFinanceOper liasFinanceOper) {
//        this.createLiasOper(liasFinanceOper.attr(LiasOpersConst.LIAS_SUMM_CLASS),
//                liasFinanceOper.attr(LiasOpersConst.LIAS_DATE_CLASS),
//                liasFinanceOper.attr(LiasOpersConst.LIAS_FINOPER_CODE_CLASS),
//                liasFinanceOper.attr(LiasOpersConst.LIAS_TYPE_ID_CLASS),
//                liasFinanceOper.attr(LiasOpersConst.LIAS_ACTION_TYPE_ID_CLASS),
//                null);
//    }
    //==========================================================================
    public void createLiasOper(
            final BigDecimal liasOperSum,
            final LiasFinanceOper liasFinanceOper,
            final AbstractEntityContract contract) {

        final LiasDocumentBuilders db = ServiceLocator.<LiasDocumentBuilders>findService(LiasDocumentBuilders.class);

        this.createLiasOper(liasOperSum,
                liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_DATE_CLASS),
                liasFinanceOper.<Integer>attr(LiasOpersConst.LIAS_FINOPER_CODE_CLASS),
                liasFinanceOper.<Integer>attr(LiasOpersConst.LIAS_TYPE_ID_CLASS),
                liasFinanceOper.<Integer>attr(LiasOpersConst.LIAS_ACTION_TYPE_ID_CLASS),
                db.createDocument(doc -> doc.setEntity(contract), liasFinanceOper));
    }
}
