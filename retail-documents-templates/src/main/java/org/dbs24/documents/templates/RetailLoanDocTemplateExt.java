/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.documents.templates;

import org.dbs24.references.api.DocTemplateId;
import org.dbs24.references.api.DocumentsConst;
import org.dbs24.references.documents.docattr.DocAttrConst;
import org.dbs24.references.documents.doctemplate.DocTemplate;

/**
 *
 * @author kazyra_d
 */
@DocTemplateId(
        doc_template_id = DocumentsConst.DTR_1001,
        doc_template_group_id = 1000,
        doc_template_code = "code1001",
        pmt_sys_id = 10000,
        doc_type_id = 1,
        doc_template_name = "dtn2001",
        //doc_attrs_collection_class = RetailDocAttrsCollection.class,
        attrsList = {DocAttrConst.LIASDATE,
            DocAttrConst.OPER_NOTES,
            DocAttrConst.LIASSUM,
            DocAttrConst.OPER_CURRENCY}
)
public class RetailLoanDocTemplateExt extends DocTemplate {
    
}
