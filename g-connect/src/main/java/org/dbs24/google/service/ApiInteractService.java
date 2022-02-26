package org.dbs24.google.service;

import com.github.yeriomin.playstoreapi.GooglePlayAPIExtended;
import com.github.yeriomin.playstoreapi.exception.account.ApiBuilderException;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.dto.GmailAccountInfo;

import java.io.IOException;

public interface ApiInteractService {

    GooglePlayAPIExtended getGPlayApi(ExecOrderAction execOrderAction) throws IOException, ApiBuilderException;

    void updateTokenIfExpired(GooglePlayAPIExtended api, GmailAccountInfo gmailAccountInfo);
}
