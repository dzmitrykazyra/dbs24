package org.dbs24.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.ContractType;
import org.dbs24.entity.DeviceType;
import org.dbs24.entity.TariffPlan;
import org.dbs24.entity.TariffPlanHist;
import org.dbs24.repository.TariffPlanHistRepository;
import org.dbs24.repository.TariffPlanRepository;
import org.dbs24.rest.api.AllTariffPlans;
import org.dbs24.rest.api.CreatedTariffPlan;
import org.dbs24.rest.api.TariffPlanInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.TariffPlanValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.WaConsts.References.CT_TRIAL;
import static org.dbs24.consts.WaConsts.References.TPS_ACTIVE;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_GENEARL_ERROR;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_OK;

@Getter
@Log4j2
@Component
public class TariffPlansService extends AbstractApplicationService {

    final TariffPlanRepository tariffPlanRepository;
    final TariffPlanHistRepository tariffPlanHistRepository;
    final RefsService refsService;
    final TariffPlanValidator tariffPlanValidator;

    @Value("${config.wa.user.trial.length:72}")
    private Integer trialLength;

    @Value("${config.wa.user.trial.future-offset:124}")
    private Integer futureOffset;

    public TariffPlansService(TariffPlanRepository tariffPlanRepository, TariffPlanHistRepository tariffPlanHistRepository, RefsService refsService, TariffPlanValidator tariffPlanValidator) {
        this.tariffPlanRepository = tariffPlanRepository;
        this.tariffPlanHistRepository = tariffPlanHistRepository;
        this.refsService = refsService;
        this.tariffPlanValidator = tariffPlanValidator;
    }

    static final Comparator<TariffPlan> latestTariffPlanActualDateSort = (a, b) -> b.getActualDate().compareTo(a.getActualDate());

    //==========================================================================
    @FunctionalInterface
    public interface TariffPlanHistBuilder {
        void buildTariffPlanHist(TariffPlan tariffPlan);
    }

    final Supplier<TariffPlan> newTariffPlan = () -> createEmptyTariffPlan();

    public Supplier<TariffPlan> buildDefaultTariffPlan(DeviceType deviceType) {

        final TariffPlan tariffPlan = createEmptyTariffPlan();

        tariffPlan.setContractType(refsService.findContractType(CT_TRIAL));
        tariffPlan.setActualDate(LocalDateTime.now());
        tariffPlan.setDurationHours(trialLength);
        tariffPlan.setSubscriptionsAmount(INTEGER_ONE);
        tariffPlan.setTariffPlanStatus(refsService.findTariffPlanStatus(TPS_ACTIVE));
        tariffPlan.setFutureOffset(futureOffset);
        tariffPlan.setDeviceType(deviceType);

        log.warn("apply default tariffPlan 4 trial contract: {}", tariffPlan);

        return () -> tariffPlan;
    }

    final BiFunction<TariffPlanInfo, TariffPlanHistBuilder, TariffPlan> assignTariffPlanInfo = (tariffPlanInfo, tariffPlanHistBuilder) -> {

        final TariffPlan tariffPlan = Optional.ofNullable(tariffPlanInfo.getTariffPlanId())
                .map(this::findTariffPlan)
                .orElseGet(newTariffPlan);

        // store history
        StmtProcessor.ifNotNull(tariffPlan.getTariffPlanId(), () -> tariffPlanHistBuilder.buildTariffPlanHist(tariffPlan));

        tariffPlan.setTariffPlanStatus(getRefsService().findTariffPlanStatus(tariffPlanInfo.getTariffPlanStatusId()));
        tariffPlan.setContractType(getRefsService().findContractType(tariffPlanInfo.getContractTypeId()));
        StmtProcessor.ifNotNull(tariffPlanInfo.getDeviceTypeId(), dt -> tariffPlan.setDeviceType(getRefsService().findDeviceType(dt)));
        tariffPlan.setSku(tariffPlanInfo.getSku());
        tariffPlan.setDurationHours(tariffPlanInfo.getDurationHours());
        tariffPlan.setSubscriptionsAmount(tariffPlanInfo.getSubscriptionsAmount());
        tariffPlan.setActualDate(NLS.long2LocalDateTime(tariffPlanInfo.getActualDate()));

        return tariffPlan;
    };

    @Transactional
    public CreatedTariffPlan createOrUpdateTariffPlan(TariffPlanInfo tariffPlanInfo) {

        return StmtProcessor.create(CreatedTariffPlan.class, createdTariffPlan -> {

            tariffPlanValidator.validateConditional(tariffPlanInfo, tpi -> {

                createdTariffPlan.setAnswerCode(OC_GENEARL_ERROR);
                createdTariffPlan.setTariffPlanId(INTEGER_NULL);
                createdTariffPlan.setNote("Can't create tariffPlan");

                final TariffPlan tariffPlan = findOrCreateTariffPlan(tpi, tp -> saveTariffPlanHist(createTariffPlanHist(tp)));

                final Boolean isNewTariffPlan = StmtProcessor.isNull(tariffPlan.getTariffPlanId());

                tariffPlanRepository.save(tariffPlan);

                createdTariffPlan.setTariffPlanId(tariffPlan.getTariffPlanId());

                final String finalMessage = String.format("Tariff plan is %s (tariffPlanId=%d)",
                        isNewTariffPlan ? "created" : "updated",
                        tariffPlan.getTariffPlanId());

                log.debug(finalMessage);

                createdTariffPlan.setAnswerCode(OC_OK);
                createdTariffPlan.setNote(finalMessage);

            }, errorInfos -> {

                createdTariffPlan.setAnswerCode(OC_GENEARL_ERROR);
                createdTariffPlan.setTariffPlanId(INTEGER_NULL);
                createdTariffPlan.setNote(errorInfos.stream().findFirst().orElseThrow().getErrorMsg().toUpperCase());

            }, throwable -> createdTariffPlan.setNote(buildDefaultErrMsg(throwable)));
        });
    }

    @Transactional(readOnly = true)
    public AllTariffPlans getTariffPlans(Boolean allDevices, Integer deviceTypeId, Boolean allPlanStatuses, Integer planStatusId) {

        log.debug("getTariffPlans: allDevices: {}, deviceTypeId: {}, allPlanStatuses: {}, planStatusId: {}", allDevices, deviceTypeId, allPlanStatuses, planStatusId);

        return StmtProcessor.create(AllTariffPlans.class, atp -> findTariffPlans(allDevices, deviceTypeId, allPlanStatuses, planStatusId)
                .stream()
                .forEach(tariffPlan -> atp.getPlans().add(StmtProcessor.create(TariffPlanInfo.class, tpi -> tpi.assign(tariffPlan)))));
    }

    //==========================================================================
    public Collection<TariffPlan> findTariffPlans(Boolean allDevices, Integer deviceTypeId, Boolean allPlanStatuses, Integer planStatusId) {
        return tariffPlanRepository.findTariffPlans(allDevices.compareTo(false), deviceTypeId, allPlanStatuses.compareTo(false), planStatusId);
    }
    //==========================================================================
    public TariffPlan findActualTariffPlan(DeviceType deviceType, ContractType contractType) {
        return findTariffPlans(BOOLEAN_FALSE, deviceType.getDeviceTypeId(), BOOLEAN_FALSE, TPS_ACTIVE)
                .stream()
                .filter(tp -> tp.getContractType().getContractTypeId().equals(contractType.getContractTypeId()))
                .sorted(latestTariffPlanActualDateSort)
                .limit(1)
                .findFirst()
                .orElseGet(buildDefaultTariffPlan(deviceType));
    }

    public TariffPlan createEmptyTariffPlan() {
        return StmtProcessor.create(TariffPlan.class, a -> a.setActualDate(LocalDateTime.now()));
    }

    public TariffPlan findOrCreateTariffPlan(TariffPlanInfo tariffPlanInfo, TariffPlanHistBuilder tariffPlanHistBuilder) {
        return assignTariffPlanInfo.apply(tariffPlanInfo, tariffPlanHistBuilder);
    }

    public TariffPlan findTariffPlan(Integer tariffPlanId) {

        return tariffPlanRepository.findById(tariffPlanId).orElseThrow(() -> new RuntimeException(String.format("Unknown tariff planId - '%d'", tariffPlanId)));
    }

    private void saveTariffPlanHist(TariffPlanHist tariffPlanHist) {
        tariffPlanHistRepository.save(tariffPlanHist);
    }

    private TariffPlanHist createTariffPlanHist(TariffPlan tariffPlan) {
        return StmtProcessor.create(TariffPlanHist.class, tph -> {
            tph.setTariffPlanId(tariffPlan.getTariffPlanId());
            tph.setContractType(tariffPlan.getContractType());
            tph.setActualDate(tariffPlan.getActualDate());
            tph.setTariffPlanStatus(tariffPlan.getTariffPlanStatus());
            tph.setDeviceType(tariffPlan.getDeviceType());
            tph.setDurationHours(tariffPlan.getDurationHours());
            tph.setSku(tariffPlan.getSku());
            tph.setSubscriptionsAmount(tariffPlan.getSubscriptionsAmount());
        });
    }
}
