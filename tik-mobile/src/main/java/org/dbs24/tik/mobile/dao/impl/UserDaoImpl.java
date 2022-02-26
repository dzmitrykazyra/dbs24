package org.dbs24.tik.mobile.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserHist;
import org.dbs24.tik.mobile.repo.UserHistRepo;
import org.dbs24.tik.mobile.repo.UserRepo;
import org.dbs24.tik.mobile.service.exception.http.NoContentException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;


@Log4j2
@Component
public class UserDaoImpl implements UserDao {

    private final UserRepo userRepo;
    private final UserHistRepo userHistRepo;

    public UserDaoImpl(UserRepo userRepo, UserHistRepo userHistRepo) {

        this.userRepo = userRepo;
        this.userHistRepo = userHistRepo;
    }

    @Override
    public User save(User userToSave) {

        return userRepo.save(userToSave);
    }

    @Override
    public User findById(Integer userId) {

        return findOptionalById(userId).orElseThrow(() -> new RuntimeException("No such user in repo layer"));
    }

    @Override
    public Optional<User> findOptionalByUsername(String tiktokAccountUsername) {

        return userRepo.findByUsername(tiktokAccountUsername);
    }

    @Override
    public Optional<User> findOptionalById(Integer userId) {

        return userRepo.findById(userId);
    }

    @Override
    public Optional<User> findOptionalByEmail(String email) {

        return userRepo.findByEmail(email);
    }

    @Override
    public User update(User userToUpdate) {

        User user = userRepo.findById(userToUpdate.getId()).orElseThrow(NoContentException::new);

        userHistRepo.save(UserHist.of(user));
        userToUpdate.setActualDate(LocalDateTime.now());

        return save(userToUpdate);
    }
}