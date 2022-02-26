/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.Data;

@Data
public class CsrfRecords {

    private StringBuilder cookie = new StringBuilder();
    private String publicKey;
    private Integer keyId;
    private String version;
    private String deviceId;
    private String csrfToken;
    private String xInstagramAjax;

}
