/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

//import org.dbs24.entity.contract.EntityContractAbstract;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.lias.opers.attrs.*;
import java.util.Collection;
import java.time.LocalDate;
import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.core.api.AllowedMethod;
import org.dbs24.entity.core.api.LiasContractAction;

import org.dbs24.lias.opers.napi.OperAttr;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.lias.opers.api.LiasOpersConst;
import lombok.Data;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.dbs24.service.ContractLiasesBuilders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author kazyra_d
 */
// абстрактное действие создающие финансовые операции над договором
@Data
@LiasContractAction
//@PreViewDialog(dialog_name = "create_contract_lias_opers")
//@AskDateDialog(dialogName = "get_calc_dates")
@AllowedMethod(action = AbstractLiasContractOper.class, entity = AbstractEntityContract.class)
public abstract class AbstractLiasContractOper<T extends AbstractEntityContract>
        extends AbstractContractDocumentAction<T> {

    @Autowired
    ContractLiasesBuilders contractLiasesBuilders;

    @Value("${defaultDocTemplate}")
    private Integer defaultDocTemplate = 1;
    // коллекция для создания финопераций
    final private Collection<LiasFinanceOper> newOpers
            = ServiceFuncs.<LiasFinanceOper>createCollection();
    // дата начисления
    private LocalDate accretionDate = LocalDate.now();
    // дата исполнения
    private LocalDate liasDate = LocalDate.now();
    // коллекция шаблонов для создания финопераций
//    private final Collection<Class<AbstractLiasOpersTemplate>> liasOpersTemplates
//            = ReflectionFuncs.<Class<AbstractLiasOpersTemplate>>createPkgClassesCollection(LiasesConst.LIASES_TEMLATE_PKG, AbstractLiasOpersTemplate.class);

    //==========================================================================
    //==========================================================================
//    public static Boolean isAllowed( AbstractEntityContract entity) {
//
//        return entity.getIsAuthorized();
//        //return true;
//    }
    //==========================================================================
    @Override
    public void initialize() {

        super.initialize();
        // запрос даты расчета операции

        // код для дополниельной инициализации действия при тестировании
//        if (TestConst.TEST_MODE_RUNNING) {
//            this.getEntity().processActionTest_afterInitialize(this);
//        }
    }

    //==========================================================================
    @Override
    protected void doCalculation() {

//        LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this),
//                String.format("LiasDate:%s, AccretionDate:%s", liasDate, accretionDate));
        this.preCalculation();

        if (!(NullSafe.notNull(this.getNewOpers()) && (!this.getNewOpers().isEmpty()))) {

            throw new LiasActionsNotSpecified("Нет операций для выполнения!");
        }
        // обработка новых операций
        contractLiasesBuilders.applyNewLiasOpers(this.getContractEntity(), this.getNewOpers());
    }
    //==========================================================================

    protected void applyNewOper(LiasFinanceOper liasFinanceOper) {

        // знак операции (увеличение\уменьшение обязательства)
    }

    //==========================================================================
    protected void addNewLiasOper(LiasFinanceOper lio) {
        lio.<ROW_NUM>addAttr(() -> Integer.valueOf(this.getNewOpers().size() /*+ 1*/));

        // код шаблона документа не задан
        if ((NullSafe.isNull(lio.<Integer>attr(LiasOpersConst.DOC_TEMPLATE_ID_CLASS)))
                && (NullSafe.notNull(this.getDefaultDocTemplate()))) {
            // берем код документа по умолчанию
//            lio.<DOC_TEMPLATE_ID>addAttr(() -> Integer.valueOf(ServiceLocator
//                    .find(DocumentReferencesService.class)
//                    .getDocTemplateById(this.getDefaultDocTemplateId())
//                    .getDoc_template_id()));
            lio.<DOC_TEMPLATE_ID>addAttr(() -> this.getDefaultDocTemplate());

        }
        this.getNewOpers().add(lio);

        if (this.getEntityCoreDebug()) {
            lio.printOperAttrsCollection();
        }
        //      }

//        LogGate.LogInfo(this.getClass(), String.format("create new liasoper (%d)",
//                this.getOpers4creation().size()));
    }

    //--------------------------------------------------------------------------
    // сохранение созданных\измененных обязательств
    //--------------------------------------------------------------------------
//    @Override
//    protected void doUpdate() {
//        super.doUpdate();
//        NullSafe.create()
//                .execute(() -> {
//                    // сохранение обязательств
//                    this.getContractEntity()
//                            .getContractLiasDebts()
//                            .bind(this.getDocument4creation())
//                            .store();
//                }).throwException();
    // создание документов
//    }
    //==========================================================================
    protected abstract void preCalculation();

    //==========================================================================
    @Override
    protected void afterCalculation() {
        super.afterCalculation();
    }

    //==========================================================================
    // присвоение финансовой операции кода шаблона
    // может быть перекрыто в наследнике
//    protected void assignDocTemplate( OldLiasOper liasFinanceOper) {
//        // по умолчанию - 1 операция - 1 документ
//        // берем из аннотации на классе
//        //liasFinanceOper.setDocTemplateId(1);
//        liasFinanceOper.<DOC_TEMPLATE_ID>addAttr(() -> Integer.valueOf(1));
//    }
    //==========================================================================
    // инициализация атрибутов документа
    // инициализация формы предварительного просмотра
    //--------------------------------------------------------------------------
//    @Override
//    protected void initPreviewForm() throws InternalAppException {
//
//    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    // для процедуры тестирования (с целью найти операцию и заменить в ней что-нибудь)
//    public final OldLiasOper findNewLiasOper(
//            final Integer liasFinOperCode,
//            final Integer liasActionTypeId) {
//
//        return (OldLiasOper) NullSafe.create()
//                .execute2result(() -> {
//
//                    final List<OldLiasOper> list
//                            = this.getNewOpers()
//                                    .stream()
//                                    .unordered()
//                                    .filter(oper -> oper.<Integer>attr(LiasOpersConst.LIAS_FINOPER_CODE_CLASS).equals(liasFinOperCode))
//                                    .filter(oper -> oper.<Integer>attr(LIAS_ACTION_TYPE_ID.class).equals(liasActionTypeId))
//                                    .collect(Collectors.toList());
//
////                    if (list.isEmpty()) {
////                        throw new LiasOperNotFoundException(String.format("LiasOper is not found(liasFinOperCode = %d, liasActionTypeId = %d)", liasFinOperCode, liasActionTypeId));
////                    }
////
////                    if (list.size() > 1) {
////                        throw new DuplicatedLiasOperException(String.format("More then opers found(liasFinOperCode = %d, liasActionTypeId = %d)", liasFinOperCode, liasActionTypeId));
////                    }
//
//                    return list.get(0);
//
//                }).<T>getObject();
//    }
    //==========================================================================    
    //==========================================================================
    @Override
    protected void finallyExecute() {
        super.finallyExecute();
        this.getNewOpers().clear();
    }

//    protected void setNewOpers( Collection<NewLiasOper> newOpers) {
//        this.newOpers = newOpers;
//    }
    // присвоение финансовой операции кода шаблона
    // может быть перекрыто в наследнике
    protected Integer getDefaultDocTemplate() {
        // по умолчанию - 1 операция - 1 документ
        // берем из аннотации на классе
        //liasOperInfo.setDocTemplateId(1);
        return defaultDocTemplate;
    }

    //==========================================================================
    protected String getMvmParam(Class<? extends OperAttr> clazz) {
        final String s;

        if (NullSafe.isNull(this.getMvm())) {
            s = STRING_NULL;
        } else {
            s = (String) ((List) this.getMvm().get(clazz.getSimpleName())).iterator().next();
        }

        return s;
    }

    //==========================================================================
    protected String getMvmParamDef(Class<? extends OperAttr> clazz,
            final String defString) {
        final String s = getMvmParam(clazz);

        return NullSafe.isNull(s) ? defString : s;
    }
}

class LiasActionsNotSpecified extends RuntimeException {

    public LiasActionsNotSpecified(String message) {
        super(message);
    }
}
