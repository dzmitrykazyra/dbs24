package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;

public interface UserProfileDetailDao {

    UserProfileDetail save(UserProfileDetail userDetailToSave);

    UserProfileDetail update(UserProfileDetail userDetailToUpdate);

    UserProfileDetail findByUserId(Integer userId);
}
