/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.contracts.actions;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.entity.contracts.AbstractEntityServiceContract;
import com.kdg.fs24.entity.document.Document;
import java.util.Collection;

import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
// абстрактное действие создающие платежные инструкции по договору
@Data
public abstract class AbstractContractDocumentAction<T extends AbstractEntityServiceContract>
        extends AbstractContractAction<T> {

    // коллекция для создания документов
    private final Collection<Document> document4creation = ServiceFuncs.<Document>createCollection();

    private Integer defaultDocTemplateId; // код шаблона документа по умолчанию
    private Integer defaultPmtSysId; // код платежной системы по умолчанию
    private Integer selectedPmtSysId; // выбранный пользователем код платежной системы
    private Long parentDocId = null;

    protected void addDocument(Document document) {
        getDocument4creation().add(document);
        //LogGate.LogInfo(this.getClass(), String.format("addDocument (%d)", this.getDocument4creation().size()));
    }

    @Override
    public void initialize() {
//        defaultPmtSysId = ServiceLocator
//                .find(ApplicationSetup.class)
//                .getRegParam("DefaultPaymentId", 1);
        // по умолчанию присваиваем выбранной платежной системе
        selectedPmtSysId = defaultPmtSysId;
    }

    //==========================================================================
    private void isValidDocuments(final Integer template_id) {

    }

    //==========================================================================
    public Collection<Document> getDocument4creation() {
        return document4creation;
    }

    public Integer getSelectedPmtSysId() {
        return selectedPmtSysId;
    }

    public void setSelectedPmtSysId(final Integer selectedPmtSysId) {
        this.selectedPmtSysId = selectedPmtSysId;
    }

    public Integer getDefaultDocTemplateId() {
        return defaultDocTemplateId;
    }

    public void setDefaultDocTemplateId(final Integer defaultDocTemplateId) {
        this.defaultDocTemplateId = defaultDocTemplateId;
    }

    //==========================================================================
//    @Override
//    protected void doUpdate() {
//        super.doUpdate();
//        NullSafe.create()
//                .execute(() -> {
//
////                    LogGate.LogInfo(this.getClass(), String.format("Document4creation.size()=(%d)",
////                            this.getDocument4creation().size()));
//                    if (!this.getDocument4creation().isEmpty()) {
//                        // добавляем документы
//
//                        this.getDocument4creation()
//                                .stream()
//                                .unordered()
//                                .forEach((docs) -> {
//
//                                    final long newDocId
//                                            = ServiceLocator
//                                                    .find(AbstractJdbcService.class)
//                                                    .createCallQuery("{:RES = call documents_insertorupdate_document(:ID, :P_ID, :DTID, :DS, :ENT, :DD, :DCD, :UID)}")
//                                                    .setParamByNameAsOutput("RES", Long.MIN_VALUE)
//                                                    .setParamByName("ID", SysConst.LONG_ZERO)
//                                                    .setParamByName("P_ID", this.getParentDocId())
//                                                    .setParamByName("DTID", docs.getDoc_template().getDoc_template_id())
//                                                    .setParamByName("DS", docs.getDoc_status().getDoc_status_id())
//                                                    .setParamByName("ENT", this.getEntity().getContract_id())
//                                                    .setParamByName("DD", docs.getDoc_date())
//                                                    .setParamByName("DCD", docs.getClose_date())
//                                                    .setParamByName("UID", this.user_id)
//                                                    .<Long>getSingleFieldValue();
//
//                                    docs.setDocId(newDocId);
//
//                                    if (NullSafe.isNull(this.getParentDocId())) {
//                                        this.setParentDocId(newDocId);
//                                    }
//
////                                    LogGate.LogInfo(this.getClass(), String.format("create new doc_id (%s, %s)",
////                                            newDocId,
////                                            this.getDocument4creation().size()));
//                                    // final Collection<DocAttrValue> doc_attrs = docs.getDoc_attrs();
//                                    // добавляем атрибуты документов
//                                    ServiceLocator.find(AbstractJdbcService.class)
//                                            .createCallBath("{call documents_insertorupdate_document_attrs(:ID, :ATTR, :AV)}")
//                                            .execBatch(stmt -> {
//
//                                                docs.getDoc_attrs()
//                                                        .stream()
//                                                        .unordered()
//                                                        .forEach((attr) -> {
//                                                            stmt.setParamByName("ID", newDocId);
//                                                            stmt.setParamByName("ATTR", attr.getDocAttrId());
//                                                            stmt.setParamByName("AV", (String) attr.getDocAttrValue());
//                                                            stmt.addBatch();
//                                                        });
//                                            });
//                                });
//                    }
//                }).throwException();
//    }

    //==========================================================================
    @Override
    protected void finallyExecute() {
        super.finallyExecute();
        this.getDocument4creation().clear();
    }

    public Long getParentDocId() {
        return parentDocId;
    }

    public void setParentDocId(final Long parentDocId) {
        this.parentDocId = parentDocId;
    }
}
