/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.api;

import com.kdg.fs24.references.documents.doctemplate.DocTemplate;

/**
 *
 * @author kazyra_d
 */
public class DocumentsConst {

    public static final Integer DS_CANCELLED = Integer.valueOf(-1);
    public static final Integer DS_CREATED = Integer.valueOf(0);
    public static final Integer DS_POSTPONED = Integer.valueOf(4001);
    public static final Integer DS_EXECUTED = Integer.valueOf(1);

    public static final Integer PS_SWIFT = Integer.valueOf(1000);
    public static final Integer PS_BISC = Integer.valueOf(10000);
    public static final Integer PS_OPL = Integer.valueOf(20000);
    public static final Integer PS_WM = Integer.valueOf(30000);    
    public static final Integer PS_YND = Integer.valueOf(40000); 
    public static final Integer PS_BTC = Integer.valueOf(50000);

    public static final Integer DT_LOAN_DEPT = Integer.valueOf(9);
    public static final Integer DT_UB = Integer.valueOf(6);

    public static final String DOCS_TEMPLATES_CLASSES_PACKAGE = "com.kdg.fs24.documents.templates";
    public static final String DOCS_REF_PACKAGE = "com.kdg.fs24.references.documents";

    public static final Integer DTG_LOAN_GROUP_JL = Integer.valueOf(1000);
    public static final Integer DTG_LOAN_GROUP_FL = Integer.valueOf(2000);
    
    public static final Class<DocTemplate> DOC_TEMPLATE_CLASS = DocTemplate.class;
    public static final Class<DocTemplateId> DOC_TEMPLATE_ID_ANN = DocTemplateId.class;
    //public static final Class<DOC_TEMPLATE_ID> DOC_TEMPLATE_ID_CLASS = DOC_TEMPLATE_ID.class;

    public static final int DTR_BASE = 1;
    public static final int DTR_1001 = 1001;

}
