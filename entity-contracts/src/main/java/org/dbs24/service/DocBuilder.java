/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.entity.document.Document;
import org.dbs24.lias.opers.napi.LiasFinanceOper;

/**
 *
 * @author Козыро Дмитрий
 */
@FunctionalInterface
public interface DocBuilder {

    void buldDocument(Document document);
}
