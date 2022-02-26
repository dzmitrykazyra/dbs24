/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.notifier.dao.MessageDao;
import org.dbs24.app.notifier.entity.Message;
import org.dbs24.app.notifier.entity.dto.CreatedMessage;
import org.dbs24.app.notifier.entity.dto.MessageDto;
import org.dbs24.app.notifier.entity.dto.MessagesList;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.rest.api.service.AbstractRestApplicationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.application.core.nullsafe.NullSafe.nvl;
import static org.dbs24.application.core.service.funcs.StringFuncs.stringVersion2long;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.SysConst.STRING_NULL;
import static org.dbs24.rest.api.consts.RestApiConst.OperCode.OC_GENEARL_ERROR;
import static org.dbs24.stmt.StmtProcessor.*;
import static reactor.core.publisher.Mono.just;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class MessagesService extends AbstractRestApplicationService {

    final MessageDao messageDao;
    final RefsService refsService;
    final ModelMapper modelMapper;

    public MessagesService(MessageDao messageDao, RefsService refsService, ModelMapper modelMapper) {
        this.messageDao = messageDao;
        this.refsService = refsService;
        this.modelMapper = modelMapper;
    }

    final Supplier<Message> createNewMessage = () -> create(Message.class);

    final Function<MessageDto, Message> assignMessageDto = messageDto -> {

        final var message = ofNullable(messageDto.getMessageId())
                .map(this::findMessage)
                .orElseGet(createNewMessage);

        getModelMapper().map(messageDto, message);

        return message;
    };

    @Transactional(readOnly = true)
    public MessagesList getMessages(Long startDate, String loginToken, String appPackage, String appVersion) {

        log.debug("getMessages: {}, {}, {}, {}", startDate, loginToken, appPackage, appVersion);

        final Predicate<Message> loginTokenCheck = message ->
                isNull(message.getMsgAddress()) ? BOOLEAN_TRUE :
                        (isNull(loginToken) ? isNull(message.getMsgAddress()) : message.getMsgAddress().contains(loginToken));

        final Predicate<Message> packageCheck = message ->
                isNull(message.getPackagesList()) ? BOOLEAN_TRUE :
                        (isNull(appPackage) ? isNull(message.getPackagesList()) : message.getPackagesList().contains(appPackage));

        final Predicate<Message> minVersionCheck = message ->
                isNull(message.getPackagesMinVersion()) ? BOOLEAN_TRUE :
                        (isNull(appVersion) ? isNull(message.getPackagesMinVersion()) : stringVersion2long(message.getPackagesMinVersion()).compareTo(stringVersion2long(appVersion)) <= 0);

        final Predicate<Message> maxVersionCheck = message ->
                isNull(message.getPackagesMaxVersion()) ? BOOLEAN_TRUE :
                        (isNull(appVersion) ? isNull(message.getPackagesMaxVersion()) : stringVersion2long(message.getPackagesMaxVersion()).compareTo(stringVersion2long(appVersion)) >= 0);

        var currentDateTime = now();

        final Predicate<Message> multiplyCheck = message -> {

            var validByCurrentDateTime = currentDateTime.compareTo(message.getActualDateFrom()) >= 0 && currentDateTime.compareTo(message.getActualDateTo()) <= 0;

            return message.getIsMultiplyMessage() ? validByCurrentDateTime :
                    (validByCurrentDateTime && nvl(long2LocalDateTime(startDate), currentDateTime).compareTo(message.getActualDateFrom()) < 0);

        };

        return create(MessagesList.class, lai ->
                lai.setMessages(ServiceFuncs.createCollection(collection ->

                        collection.addAll(
                                messageDao.findLatestMessages(nvl(long2LocalDateTime(startDate), now().minusHours(1)))
                                        .stream()
                                        //.filter(message -> isNull(message.getMsgAddress()) || message.getMsgAddress().equals(msgKey))
                                        .filter(Message::getIsActual)
                                        .filter(loginTokenCheck)
                                        .filter(packageCheck)
                                        .filter(minVersionCheck)
                                        .filter(maxVersionCheck)
                                        .filter(multiplyCheck)
                                        .sorted(Comparator.comparing(Message::getCreateDate))
                                        .map(this::createDto)
                                        .collect(Collectors.toList()))))
        );
    }

    @Transactional
    public Mono<CreatedMessage> createOrUpdateMessage(MessageDto messageDto) {

        return just(create(CreatedMessage.class, createdMessage -> {

            createdMessage.setNote(STRING_NULL);

            // field validation
            just(messageDto)
                    .subscribe(dto -> {

                        ifNull(messageDto.getCreateDate(), () -> messageDto.setCreateDate(localDateTime2long(now())));

                        ifNull(messageDto.getActualDateFrom(), () -> createdMessage.setNote("field actualDateFrom not defined"));
                        ifNull(messageDto.getActualDateTo(), () -> createdMessage.setNote("field actualDateTo not defined"));
                        ifNull(messageDto.getMessageDetails(), () -> createdMessage.setNote("field messageBody not defined"));

                        ifTrue(notNull(messageDto.getActualDateFrom()) && notNull(messageDto.getActualDateTo()),
                                () -> ifTrue(!(messageDto.getActualDateFrom().compareTo(messageDto.getActualDateTo()) < 0),
                                        () -> createdMessage.setNote("actualDateFrom -> actualDateTo order violation")));


                        // validate PackagesMinVersion
                        ifNotNull(messageDto.getPackagesMinVersion(), () -> stringVersion2long(messageDto.getPackagesMinVersion()));
                        // validate PackagesMaxVersion
                        ifNotNull(messageDto.getPackagesMaxVersion(), () -> stringVersion2long(messageDto.getPackagesMaxVersion()));

                        ifTrue(notNull(messageDto.getPackagesMinVersion()) && notNull(messageDto.getPackagesMaxVersion()),
                                () -> ifTrue(!(stringVersion2long(messageDto.getPackagesMinVersion()).compareTo(stringVersion2long(messageDto.getPackagesMaxVersion())) <= 0),
                                        () -> createdMessage.setNote(String.format("packages version order violation (%s -> %s)",
                                                messageDto.getPackagesMinVersion(),
                                                messageDto.getPackagesMaxVersion())))
                        );

                    }, throwable -> {
                        createdMessage.setAnswerCode(OC_GENEARL_ERROR);
                        createdMessage.setNote(String.format("%s: %s", throwable.getClass(), throwable.getMessage()));
                        log.error(createdMessage.getNote());
                    });

            ifNull(createdMessage.getNote(), () -> just(messageDto)
                    .subscribe(dto -> {

                        final var message = findOrCreateMessages(dto);

                        final var isNewSetting = isNull(message.getMessageId());

                        messageDao.saveMessage(message);

                        final String finalMessage = format("Message is %s (messageId=%d)",
                                isNewSetting ? "created" : "updated",
                                message.getMessageId());

                        createdMessage.setMessageId(message.getMessageId());
                        createdMessage.setNote(finalMessage);
                        log.debug(finalMessage);

                    }, throwable -> {
                        createdMessage.setAnswerCode(OC_GENEARL_ERROR);
                        createdMessage.setNote(String.format("%s: %s", throwable.getClass(), throwable.getMessage()));
                        log.error(createdMessage.getNote());
                    }), () -> {
                createdMessage.setAnswerCode(OC_GENEARL_ERROR);
                log.debug(createdMessage.getNote());
            });
        }));
    }

    public Message findOrCreateMessages(MessageDto messageDto) {
        return assignMessageDto.apply(messageDto);
    }

    public Message findMessage(Integer messageId) {
        return getMessageDao().findMessage(messageId);
    }

    private MessageDto createDto(Message message) {
        return getModelMapper().map(message, MessageDto.class);
    }
}
