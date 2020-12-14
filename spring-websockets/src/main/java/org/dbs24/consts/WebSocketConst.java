/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

public final class WebSocketConst {
    //public static final String URL_WS = "/WS"; 

    public static final String WS_PROTOCOL = "WS";
    public static final String WSS_PROTOCOL = "WSS";
    
    
    public static final String WS_ECHO = "/echo";
    public static final String WS_KEYWORD = "/websocket";    
    public static final String WS_KEYWORD_SECURITY_FILTER = WS_KEYWORD.concat("/**");
    //public static final String DEFAULT_MAPPING_PATH = "${spring.websocket.mapping-path:".concat(WS_KEYWORD).concat("}");

}
