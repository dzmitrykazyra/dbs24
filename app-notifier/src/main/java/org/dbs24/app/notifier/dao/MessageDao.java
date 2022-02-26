/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.notifier.entity.Message;
import org.dbs24.app.notifier.repo.MessageRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.dbs24.app.notifier.consts.AppNotifierConsts.Caches.CACHE_MESSAGE;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class MessageDao extends DaoAbstractApplicationService {

    final MessageRepo messageRepo;

    public MessageDao(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    //==========================================================================
    public Optional<Message> findOptionalMessage(Integer messageId) {
        return messageRepo.findById(messageId);
    }

    //
    @Cacheable(CACHE_MESSAGE)
    public Message findMessage(Integer messageId) {
        return findOptionalMessage(messageId).orElseThrow();
    }

    //
    @CacheEvict(value = {CACHE_MESSAGE}, beforeInvocation = false, key = "#message.messageId", condition = "#message.messageId>0")
    public void saveMessage(Message message) {
        messageRepo.save(message);
    }

    public Collection<Message> findLatestMessages(LocalDateTime sinceDate) {
        return messageRepo.findLatestMessages(sinceDate, now());
    }

}
