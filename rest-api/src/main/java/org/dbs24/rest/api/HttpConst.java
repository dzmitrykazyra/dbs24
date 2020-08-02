/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;
//import org.dbs24.application.core.sysconst.SysConst;

/**
 *
 * @author Козыро Дмитрий
 */
public final class HttpConst {
    
    public final static String APP_JSON = "application/json;";
    public final static String APP_XML = "application/xml";
    public final static String CONTENT_TYPE = "application/json; charset=utf-8;";
    public final static String SRV_READ_TIMEOUT = "30000";
    public final static String SRV_CONNECT_TIMEOUT = "10000";
    public final static String GZIP_ACCEPT_ENCODING = "gzip";
    public final static String IDENTITY_CONTENT_ENCODING = "identity";
    public final static Integer HTTP_200_OK = 200;
    public final static Integer HTTP_204_NO_CONTENT = 204;   
    public final static Integer HTTP_500_INTERNAL_ERROR = 500;
    
    public final static Boolean USE_GZIP_ENCODING = Boolean.TRUE;
    public final static Boolean NO_GZIP_ENCODING = Boolean.FALSE;
    
    public final static String HTTP_200_OK_STRING = "OK";
    
}
