package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.UserProfileDetailDao;
import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;
import org.dbs24.tik.mobile.repo.UserProfileDetailRepo;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDetailDaoImpl implements UserProfileDetailDao {

    private final UserProfileDetailRepo userProfileDetailRepo;

    public UserProfileDetailDaoImpl(UserProfileDetailRepo userProfileDetailRepo) {

        this.userProfileDetailRepo = userProfileDetailRepo;
    }

    @Override
    public UserProfileDetail save(UserProfileDetail userDetailToSave) {

        return userProfileDetailRepo.save(userDetailToSave);
    }

    @Override
    public UserProfileDetail update(UserProfileDetail userDetailToUpdate) {

        return userProfileDetailRepo.save(userDetailToUpdate);
    }

    @Override
    public UserProfileDetail findByUserId(Integer userId) {

        return userProfileDetailRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No details by required user id in repo layer"));
    }
}