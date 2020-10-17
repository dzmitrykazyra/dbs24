/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;
//import static org.dbs24.application.core.sysconst.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
public final class HttpConst {
    
    public static final String APP_JSON = "application/json;";
    public static final String APP_XML = "application/xml";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8;";
    public static final String SRV_READ_TIMEOUT = "30000";
    public static final String SRV_CONNECT_TIMEOUT = "10000";
    public static final String GZIP_ACCEPT_ENCODING = "gzip";
    public static final String IDENTITY_CONTENT_ENCODING = "identity";
    public static final Integer HTTP_200_OK = 200;
    public static final Integer HTTP_204_NO_CONTENT = 204;   
    public static final Integer HTTP_500_INTERNAL_ERROR = 500;
    
    public static final Boolean USE_GZIP_ENCODING = Boolean.TRUE;
    public static final Boolean NO_GZIP_ENCODING = Boolean.FALSE;
    
    public static final String HTTP_200_OK_STRING = "OK";
    
}
