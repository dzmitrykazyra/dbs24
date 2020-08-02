/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.lias.opers.attrs;

import org.dbs24.lias.opers.api.DocAttrLinkProperty;
import org.dbs24.lias.opers.napi.GenericOperAttr;
import org.dbs24.references.documents.docattr.DocAttrConst;
import java.math.BigDecimal;

/**
 *
 * @author kazyra_d
 */
@DocAttrLinkProperty(docAttr = DocAttrConst.LIASSUM)
public interface LIAS_SUMM extends GenericOperAttr<BigDecimal> {

}
