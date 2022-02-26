package org.dbs24.google.api.dto;

import lombok.Data;

@Data
public class GmailAccountInfo {
    private String email;
    private String pass;
    private String gmailToken;
    private String gplayToken;
    private String aasToken;
    private String gsfId;
    private String phoneConfigName;
}
