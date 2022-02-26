package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.User;

import java.util.Optional;

public interface UserDao {

    User save(User userToSave);

    User findById(Integer userId);

    Optional<User> findOptionalByUsername(String tiktokAccountUsername);

    Optional<User> findOptionalById(Integer userId);

    Optional<User> findOptionalByEmail(String email);

    User update(User userToUpdate);
}
