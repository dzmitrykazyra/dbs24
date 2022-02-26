package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.UserForgottenPasswordKeysetDto;

public interface UserPasswordKeyService {

    UserForgottenPasswordKeysetDto generateChangePasswordKeyset(User user);

    Integer getUserIdByChangePasswordKeyset(UserForgottenPasswordKeysetDto keyset);
}
