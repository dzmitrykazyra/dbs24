/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Козыро Дмитрий
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface DocTemplateId {

    int doc_template_id();

    int doc_template_group_id();

    String doc_template_code();

    int pmt_sys_id();

    int doc_type_id();

    String doc_template_name();

    String en_doc_template_name() default "n/a";

    //DocAttrId[] doc_attrs_list();

    //Class<? extends DocAttrsCollection> doc_attrs_collection_class();
    
    int[] attrsList();

}
