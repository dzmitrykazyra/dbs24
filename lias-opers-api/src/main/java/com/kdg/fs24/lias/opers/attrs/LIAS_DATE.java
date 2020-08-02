/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.lias.opers.attrs;

import com.kdg.fs24.lias.opers.api.DocAttrLinkProperty;
import com.kdg.fs24.lias.opers.napi.GenericOperAttr;
import com.kdg.fs24.references.documents.docattr.DocAttrConst;
import java.time.LocalDate;

/**
 *
 * @author kazyra_d
 */
@DocAttrLinkProperty(docAttr = DocAttrConst.LIASDATE)
public interface LIAS_DATE extends GenericOperAttr<LocalDate> {

}
