/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.springframework.stereotype.Service;
import org.dbs24.references.api.DocTemplateId;
import java.util.Collection;
import java.util.Arrays;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import org.dbs24.references.api.DocumentsConst;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.entity.document.Document;
import org.dbs24.entity.document.DocAttrValue;
import org.dbs24.lias.opers.napi.LiasFinanceOper;
import org.dbs24.lias.opers.attrs.DOC_TEMPLATE_ID;
import org.dbs24.application.core.locale.NLS;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.lias.opers.api.LiasOpersConst;
import org.dbs24.references.documents.docattr.DocAttr;
import org.dbs24.references.documents.docstatus.DocStatus;
import org.dbs24.references.documents.doctemplate.DocTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LiasDocumentBuilders extends AbstractApplicationBean {

    private final Collection<DocTemplateId> templatesList
            = ServiceFuncs.<DocTemplateId>createCollection();

    //==========================================================================
    @Override
    public void initialize() {

        // регистрируем коллекцию шаблонов
        ReflectionFuncs.createPkgClassesCollection(DocumentsConst.DOCS_TEMPLATES_CLASSES_PACKAGE, DocumentsConst.DOC_TEMPLATE_CLASS)
                .stream()
                .unordered()
                .filter(p -> AnnotationFuncs.isAnnotated(p, DocumentsConst.DOC_TEMPLATE_ID_ANN))
                .forEach(clazz -> templatesList
                .add(AnnotationFuncs.getAnnotation(clazz, DocumentsConst.DOC_TEMPLATE_ID_ANN))
                );
    }

    //==========================================================================
    public DocTemplateId getDocTemplateById(Integer template_id) {

        return ServiceFuncs.<DocTemplateId>findCollectionElement(this.templatesList,
                p -> template_id.equals(p.doc_template_id()),
                String.format("DocTemplate does not exists (%d)", template_id));

    }

    //==========================================================================
    public Document createDocument(DocBuilder docBuilder, LiasFinanceOper liasFinanceOper) {

        final Document document = NullSafe.createObject(Document.class);

        final Collection<DocAttrValue> dav = this.createNewDocAttrs(liasFinanceOper, document);

        document.setDocStatus(DocStatus.findDocStatus(liasFinanceOper.<Integer>attrDef(LiasOpersConst.DOC_STATUS_ID_CLASS, DocumentsConst.DS_EXECUTED)));
        document.setDocTemplate(DocTemplate.findDocTemplate(liasFinanceOper.<Integer>attrDef(LiasOpersConst.DOC_TEMPLATE_ID_CLASS, DocumentsConst.DTR_BASE)));
        document.setDocServerDate(LocalDateTime.now());
        document.setDocDate(liasFinanceOper.<LocalDate>attrDef(LiasOpersConst.LIAS_DATE_CLASS, LocalDate.now()));
        document.setUserId(SERVICE_USER_ID);
        document.setDocAttrs(dav);

        docBuilder.buldDocument(document);

        return document;

    }

    //==========================================================================
    protected Collection<DocAttrValue> createNewDocAttrs(LiasFinanceOper liasFinanceOper, Document document) {
        final DocTemplateId dti = this.getDocTemplateById(liasFinanceOper.<Integer>attr(DOC_TEMPLATE_ID.class));

        return this.processDocAttrs(dti, liasFinanceOper, document);

    }

    //==========================================================================
    private Collection<DocAttrValue> processDocAttrs(
            final DocTemplateId dti,
            final LiasFinanceOper liasFinanceOper,
            final Document document) {

        final Collection<DocAttrValue> dac = ServiceFuncs.<DocAttrValue>createCollection();

        NullSafe.create(dti)
                .safeExecute(() -> {

                    NullSafe.create()
                            .execute(() -> {

                                // добавляем в коллекцию
                                Arrays.stream(dti.attrsList())
                                        .unordered()
                                        .forEach((ai) -> {
                                            liasFinanceOper.LINKED_FIELDS
                                                    .entrySet()
                                                    .stream()
                                                    .unordered()
                                                    .filter(s -> s.getKey().equals(ai))
                                                    .forEach((field) -> {

                                                        final DocAttrValue dav = NullSafe.createObject(DocAttrValue.class);
                                                        dav.setDocAttr(DocAttr.findDocAttr(ai));
                                                        dav.setDocAttrValue((String) NLS.getObject2String(
                                                                liasFinanceOper.attrDef(field.getValue(),
                                                                        NOT_DEFINED)));
                                                        dav.setDocument(document);
                                                        dac.add(dav);
                                                    });
                                        });
                            });
                });
        return dac;
    }

//==========================================================================
    @Override
    public void destroy() {
        templatesList.clear();
    }
    //==========================================================================
}
