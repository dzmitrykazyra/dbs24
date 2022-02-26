package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.UserEmailBoundingKeysetDto;
import org.dbs24.tik.mobile.entity.dto.user.UserForgottenPasswordKeysetDto;

public interface UserEmailKeyService {

    UserEmailBoundingKeysetDto generateEmailBoundingKeyset(String email, User user);

    String getEmailByKeyBoundingSet(UserEmailBoundingKeysetDto keyset);

    Integer getUserIdByKeyBoundingSet(UserEmailBoundingKeysetDto keyset);
}
