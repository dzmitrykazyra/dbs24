/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.documents.docattr;

import com.kdg.fs24.references.api.DocAttrId;
import com.kdg.fs24.references.api.DocAttrsListId;

/**
 *
 * @author kazyra_d
 */

@DocAttrsListId(
        ids = {
            @DocAttrId(doc_attr_id = DocAttrConst.ACCRETION_DATE, doc_attr_code = DocAttrConst.ACCRETION_DATE_CODE, doc_attr_name = DocAttrConst.ACCRETION_DATE_NAME),
            @DocAttrId(doc_attr_id = DocAttrConst.LIASDATE, doc_attr_code = DocAttrConst.LIASDATE_CODE, doc_attr_name = DocAttrConst.LIASDATE_NAME),
            @DocAttrId(doc_attr_id = DocAttrConst.LIASSUM, doc_attr_code = DocAttrConst.LIASSUM_CODE, doc_attr_name = DocAttrConst.LIASSUM_NAME),
            @DocAttrId(doc_attr_id = DocAttrConst.OPER_NOTES, doc_attr_code = DocAttrConst.OPER_NOTES_CODE, doc_attr_name = DocAttrConst.OPER_NOTES_NAME),
            @DocAttrId(doc_attr_id = DocAttrConst.OPER_NOTES_TMPL, doc_attr_code = DocAttrConst.OPER_NOTES_TMPL_CODE, doc_attr_name = DocAttrConst.OPER_NOTES_TMPL_NAME),
            @DocAttrId(doc_attr_id = DocAttrConst.OPER_CURRENCY, doc_attr_code = DocAttrConst.OPER_CURRENCY_CODE, doc_attr_name = DocAttrConst.OPER_CURRENCY_NAME),
            @DocAttrId(doc_attr_id = DocAttrConst.DOC_STATUS, doc_attr_code = DocAttrConst.DOC_STATUS_CODE, doc_attr_name = DocAttrConst.DOC_STATUS_NAME)            
        }
)
public class DocAttrsRecords {
    
}
