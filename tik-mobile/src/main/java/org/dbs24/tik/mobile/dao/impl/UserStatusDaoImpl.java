package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.constant.reference.UserStatusDefine;
import org.dbs24.tik.mobile.dao.UserStatusDao;
import org.dbs24.tik.mobile.entity.domain.UserStatus;
import org.dbs24.tik.mobile.repo.UserStatusRepo;
import org.springframework.stereotype.Component;

@Component
public class UserStatusDaoImpl implements UserStatusDao {

    private final UserStatusRepo userStatusRepo;

    public UserStatusDaoImpl(UserStatusRepo userStatusRepo) {

        this.userStatusRepo = userStatusRepo;
    }

    @Override
    public UserStatus findByDefine(UserStatusDefine userStatusDefine) {

        return userStatusRepo
                .findByUserStatusName(userStatusDefine.getUserStatusName())
                .orElseThrow(() -> new RuntimeException("No usch user status record in repo layer"));
    }
}
