/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import org.dbs24.rest.api.ResponseBody;
import org.dbs24.spring.core.api.PostRequestBody;

@FunctionalInterface
public interface EntityGetProcessor<T extends PostRequestBody> {

    void build(ResponseBody restFulResult, T restFulEntity);
}
