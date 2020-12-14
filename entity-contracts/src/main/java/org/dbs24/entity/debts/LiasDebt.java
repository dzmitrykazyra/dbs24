/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.debts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dbs24.entity.Currency;
import org.dbs24.references.liases.baseassettype.LiasBaseAssetType;
import org.dbs24.references.liases.debtstate.LiasDebtState;
import org.dbs24.references.liases.kind.LiasKind;
import org.dbs24.references.liases.type.LiasType;
//import org.dbs24.entity.bondschedule.PmtSchedule;
import org.dbs24.lias.opers.api.LiasOpersConst;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.time.LocalDate;
import java.util.Collection;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.log.LogService;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import java.math.BigDecimal;
import org.dbs24.lias.opers.attrs.*;
import javax.persistence.*;
import lombok.Data;
import java.util.Optional;
import java.util.List;
import org.dbs24.application.core.service.funcs.FilterComparator;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.dbs24.entity.Counterparty;
import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.bondschedule.PmtSchedule;
import org.dbs24.service.LiasDocumentBuilders;
import org.dbs24.spring.core.api.ServiceLocator;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.application.core.locale.NLS;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Table(name = "liasDebts")
public class LiasDebt extends ObjectRoot implements PersistenceEntity {

    @Transient
    private Integer rowNum;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_debt_id")
    @SequenceGenerator(name = "seq_debt_id", sequenceName = "seq_debt_id", allocationSize = 1)
    @Column(name = "debt_id", updatable = false)
    private Integer debtId;
    //--------------------------------------------------------------------------
    @OneToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    @JsonIgnore
    private AbstractEntityContract debtContract;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "counterparty_id", referencedColumnName = "counterparty_id")
    @JsonIgnore
    private Counterparty counterparty;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "currency_id")
    private Currency currency;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "debt_state_id", referencedColumnName = "debt_state_id")
    private LiasDebtState liasDebtState;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "lias_kind_id", referencedColumnName = "lias_kind_id")
    private LiasKind liasKind;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "lias_type_id", referencedColumnName = "lias_type_id")
    private LiasType liasType;
    //--------------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "base_asset_type_id", referencedColumnName = "base_asset_type_id")
    private LiasBaseAssetType liasBaseAssetType;
    //--------------------------------------------------------------------------
    @Column(name = "debt_start_date")
    private LocalDate debtStartDate;
    //--------------------------------------------------------------------------
    @Column(name = "debt_final_date")
    private LocalDate debtFinalDate;
    //--------------------------------------------------------------------------
    //@OneToMany
    //@JoinColumn(name = "debt_id", referencedColumnName = "debt_id")
    @OneToMany(mappedBy = "liasDebt", cascade = CascadeType.ALL)
    private Collection<Lias> liases = ServiceFuncs.<Lias>createCollection();
    //--------------------------------------------------------------------------
    @org.hibernate.annotations.BatchSize(size = 100)
    @OneToMany(mappedBy = "liasDebt", cascade = CascadeType.ALL)
    private Collection<LiasDebtRest> liasDebtRests = ServiceFuncs.<LiasDebtRest>createCollection();

    //==========================================================================
    // сервисная часть
    //==========================================================================
    public final void createOrUpdateLiases(LiasFinanceOper liasFinanceOper) {

        // график погашения обязательства
        final Optional<PmtSchedule> pmtSchedule = ServiceFuncs.<PmtSchedule>getCollectionElement(debtContract.getPmtSchedules(),
                bs -> (bs.getEntityKind().getEntityKindId().equals(liasFinanceOper.<Integer>attr(PMT_SCHEDULE.class))));

        if (NullSafe.isNull(this.getLiases())) {
            this.setLiases(ServiceFuncs.<Lias>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL));
        }
        if (pmtSchedule.isPresent()) {
            // обязательства формируются согласно графика
            this.processBondSchedLiases(liasFinanceOper, pmtSchedule.get());
        } else {
            // обязательство без графика
            this.processLias(liasFinanceOper);
        }
    }

    //==========================================================================
    private void processBondSchedLiases(LiasFinanceOper liasFinanceOper, PmtSchedule pmtSchedule) {

        //----------------------------------------------------------------------
        // увеличение обязательств
        if (liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS).signum() > 0) {
            this.incrementLiases(liasFinanceOper, pmtSchedule);
        } else {
            // уменьшение обязательств
            this.decrementLiases(liasFinanceOper);
        }
    }

    // обязательство без графика
    //==========================================================================
    private void processLias(LiasFinanceOper liasFinanceOper) {

        if (liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS).signum() > 0) {
            Lias lias = this.findLias(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS),
                    liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS),
                    liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS).signum() < 0);

            // если обязательство не найдено, то создаем его
            lias = NullSafe.create(lias)
                    .whenIsNull(() -> {
                        // обязательство не найдено
                        // графика нет - формируем одно обязательство
                        final Lias newLias = this.createLias(liasFinanceOper);
                        this.liases.add(newLias);
                        return newLias;
                    }).<Lias>getObject();

            // создаем действие по обязательству
            lias.createLiasOper(liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS),
                    liasFinanceOper,
                    this.getDebtContract());
        } else {
            //this.findFirstLias().createLiasOper(liasFinanceOper);
            this.decrementLiases(liasFinanceOper);
        }
    }

    //==========================================================================
    public Lias findLias(
            LocalDate startDate,
            LocalDate finalDate,
            Boolean throwExcWhenNotFound) {

        final FilterComparator<Lias> filterComparator = l -> (l.getStartDate().equals(startDate) && l.getFinalDate().equals(finalDate));
        final Optional<Lias> lias = ServiceFuncs.<Lias>getCollectionElement(this.getLiases(),
                filterComparator);

        if (throwExcWhenNotFound && !lias.isPresent()) {

            class LiasDoesNotExists extends InternalAppException {

                public LiasDoesNotExists(String message) {
                    super(message);
                }
            }
            throw new LiasDoesNotExists(String.format("Обязательство не существует(%s,%s)", startDate, finalDate));
        }

        return lias.orElse(null);
    }

    interface LiasOperRest {

        BigDecimal getLiasOperSum();
    }

    //==========================================================================
    private void incrementLiases(LiasFinanceOper liasFinanceOper, PmtSchedule pmtSchedule) {
        //----------------------------------------------------------------------

        // сумма операции
        //анонимный класс для вычисления остатка
        final LiasOperRest liasOperRest = new LiasOperRest() {

            private BigDecimal liasOperRest = (BigDecimal) liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS);
            final private BigDecimal substrSum = liasOperRest.divide(BigDecimal.valueOf(pmtSchedule.getPmtScheduleLines().size()), 2, 2);

            @Override
            public BigDecimal getLiasOperSum() {
                final BigDecimal liasOperSum = liasOperRest.min(substrSum);
                liasOperRest = liasOperRest.subtract(substrSum);
                return liasOperSum;
            }
        };

        pmtSchedule
                .getPmtScheduleLines()
                .stream()
                .forEach((pmtScheduleLine) -> {

                    final Lias schedLias = NullSafe.create(this.findLias(pmtScheduleLine.getFromDate(),
                            pmtScheduleLine.getToDate(),
                            (liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS)).signum() < 0))
                            .whenIsNull(() -> this.createLias(pmtScheduleLine.getFromDate(),
                            pmtScheduleLine.getToDate())
                            ).<Lias>getObject();
                    schedLias.setLiasDebt(this);
                    liases.add(schedLias);

                    // создание финопераций
                    schedLias.createLiasOper(liasOperRest.getLiasOperSum(), liasFinanceOper, this.getDebtContract());
                });
    }

    //==========================================================================
    // уменьшение обязательства
    private void decrementLiases(LiasFinanceOper liasFinanceOper) {
        NullSafe.create()
                .execute(() -> {
                    // ищем обязаетельство для его уменьшения
                    //this.findFirstLias().createLiasOper(liasFinanceOper);
                    this.findFirstLias().createLiasOper(
                            liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS),
                            liasFinanceOper,
                            this.getDebtContract());
                });
    }

    //==========================================================================
    // создание нового обязательства
    private Lias createLias(LiasFinanceOper liasFinanceOper) {
        //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this));
        final Lias lias = NullSafe.createObject(Lias.class);

        lias.setStartDate(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS));
        lias.setAllowDate(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS));
        lias.setFinalDate(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_FINAL_DATE_CLASS));
        lias.setLegalDate(liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_START_DATE_CLASS));
        lias.setServerDate(LocalDateTime.now());
        lias.setIsCancelled(Boolean.FALSE);

        return lias;

    }

    //==========================================================================
    private Lias createLias(LocalDate liasStartDate, LocalDate liasFinalDate) {
        //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this));

        final Lias lias = NullSafe.createObject(Lias.class);

        lias.setStartDate(liasStartDate);
        lias.setAllowDate(liasStartDate);
        lias.setFinalDate(liasFinalDate);
        lias.setLegalDate(liasFinalDate);
        lias.setServerDate(LocalDateTime.now());
        lias.setIsCancelled(Boolean.FALSE);

        return lias;

    }

    //==========================================================================  
    public Lias findFirstLias() {

        return ServiceFuncs.<Lias>findCollectionElement(
                this.getLiases()
                        .stream()
                        .sorted((lias1, lias2) -> lias1.getStartDate().compareTo(lias2.getStartDate()))
                        .collect(Collectors.toList()),
                l -> (NullSafe.isNull(l.getInactiveDate())),
                String.format("Не найдено подходящее обязательство (DebtId=%d)", this.debtId));
    }

    //==========================================================================
    public final void createOrUpdateDebtRests(LiasFinanceOper liasFinanceOper) {
        // находим остаток за дату операции

        final LocalDate operDate = liasFinanceOper.<LocalDate>attr(LiasOpersConst.LIAS_DATE_CLASS);
        final BigDecimal operSum = liasFinanceOper.<BigDecimal>attr(LiasOpersConst.LIAS_SUMM_CLASS);

        if (!ServiceFuncs.getCollectionElement(this.getLiasDebtRests(),
                ldr -> ldr.getRestDate().equals(operDate)).isPresent()) {

            // предыдущий остаток
            final List<LiasDebtRest> lastRest = this.getLiasDebtRests()
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

            final LiasDebtRest liasDebtRest = NullSafe.createObject(LiasDebtRest.class);

            liasDebtRest.setRest(newRest);
            liasDebtRest.setRestType(INTEGER_ONE);
            liasDebtRest.setRestDate(operDate);
            liasDebtRest.setLiasDebt(this);

            // добавили новый остаток в коллекцию остатков
            this.getLiasDebtRests().add(liasDebtRest);

        }
        // Группа остатков для увеличения\уменьшения       
        this.getLiasDebtRests()
                .stream()
                .unordered()
                .filter(ldr -> ldr.getRestDate().isAfter(operDate)
                || ldr.getRestDate().equals(operDate))
                .forEach(rest -> {
                    //увеличить/уменьшить остаток
                    rest.incRest(operSum);
                });
    }

    //==========================================================================
    public void printLiasDebtRests() {
        LogService.LogInfo(this.getClass(), () -> this.liasDebtRests
                .stream()
                .sorted((r1, r2) -> r1.getRestDate().compareTo(r2.getRestDate()))
                .map(rest
                        -> String.format("restDate: %s; restSum: %f\n",
                        NLS.getStringDate(rest.getRestDate()),
                        rest.getRest()))
                .reduce(String.format("Debts rests (%d): \n",
                        this.liasDebtRests.size()),
                        (x, y) -> x.concat(y)));
    }
    //==========================================================================
}
