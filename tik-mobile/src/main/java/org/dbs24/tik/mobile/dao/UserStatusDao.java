package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.constant.reference.UserStatusDefine;
import org.dbs24.tik.mobile.entity.domain.UserStatus;

public interface UserStatusDao {

    UserStatus findByDefine(UserStatusDefine userStatusDefine);
}
