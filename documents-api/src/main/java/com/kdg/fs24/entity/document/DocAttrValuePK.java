/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.document;

import java.io.Serializable;
import lombok.Data;
import com.kdg.fs24.references.documents.docattr.DocAttr;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class DocAttrValuePK implements Serializable {

    private Document document;
    private DocAttr docAttr;
}
