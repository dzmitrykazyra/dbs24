/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.documents.templates;

import com.kdg.fs24.references.api.DocTemplateId;
import com.kdg.fs24.references.documents.docattr.DocAttrConst;
import com.kdg.fs24.references.documents.doctemplate.DocTemplate;
/**
 *
 * @author Козыро Дмитрий
 */

@DocTemplateId(
        doc_template_id = 1,
        doc_template_group_id = 1000,
        doc_template_code = "code1",
        pmt_sys_id = 10000,
        doc_type_id = 1,
        doc_template_name = "dtn0",
        //doc_attrs_collection_class = RetailDocAttrsCollection.class,
        attrsList = {DocAttrConst.LIASDATE,
            DocAttrConst.OPER_NOTES,
            DocAttrConst.LIASSUM,
            DocAttrConst.OPER_CURRENCY}
)
public abstract class DefaultDocTemplate extends DocTemplate {
    
}
