package org.dbs24.tik.assist.entity.dto.bot;

import lombok.*;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.Bot;

import java.time.LocalDateTime;

@Data
public class BotDto {

    private Integer botId;
    private Long actualDate;
    private Long createDate;
    private Integer botStatusId;
    private Integer botRegistrationTypeId;
    private String secUserId;
    private String phoneNumber;
    private String email;
    private String botNote;
    private String password;
    private byte[] attributes;

    public static BotDto toBotDto(Bot bot) {

        return StmtProcessor.create(
                BotDto.class,
                botDto -> {
                    botDto.setBotId(bot.getBotId());
                    botDto.setActualDate(NLS.localDateTime2long(bot.getActualDate()));
                    botDto.setBotStatusId(bot.getBotStatus().getBotStatusId());
                    botDto.setBotRegistrationTypeId(bot.getBotRegistrationType().getBotRegistryTypeId());
                    botDto.setCreateDate(NLS.localDateTime2long(bot.getCreateDate()));
                    botDto.setSecUserId(bot.getSecUserId());
                    botDto.setPhoneNumber(bot.getPhoneNumber());
                    botDto.setEmail(bot.getEmail());
                    botDto.setBotNote(bot.getBotNote());
                    botDto.setPassword(bot.getPass());
                    botDto.setAttributes(bot.getAttributes());
                });
    }

    /**
     * Method allows creating bot from dto with null fields:
     *      - bot status
     *      - bot registration type
     */
    public static Bot defaultBotFromDto(BotDto botDto) {

        return StmtProcessor.create(
                Bot.class,
                bot -> {
                    bot.setBotId(botDto.getBotId());
                    bot.setActualDate(LocalDateTime.now());
                    bot.setCreateDate(NLS.long2LocalDateTime(botDto.getCreateDate()));
                    bot.setSecUserId(botDto.getSecUserId());
                    bot.setPhoneNumber(botDto.getPhoneNumber());
                    bot.setEmail(botDto.getEmail());
                    bot.setBotNote(botDto.getBotNote());
                    bot.setPass(botDto.getPassword());
                    bot.setAttributes(botDto.getAttributes());
                });
    }
}