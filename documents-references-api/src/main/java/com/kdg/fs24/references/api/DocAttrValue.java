/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.api;

/**
 *
 * @author Козыро Дмитрий
 */
import com.kdg.fs24.references.documents.docattr.DocAttr;

public interface DocAttrValue {

    //DocAttr getDocAttr();
    int getDocAttrId();

    String getStringDocAttr();

    Object getDocAttrValue();

}
