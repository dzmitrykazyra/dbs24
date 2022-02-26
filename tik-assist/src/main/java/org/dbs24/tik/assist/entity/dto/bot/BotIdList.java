package org.dbs24.tik.assist.entity.dto.bot;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.Bot;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BotIdList {

    private List<Integer> botIds;

    public static BotIdList of(List<Bot> botList) {

        return StmtProcessor.create(
                BotIdList.class,
                botIdList -> botIdList.setBotIds(
                        botList.stream()
                                .map(Bot::getBotId)
                                .collect(Collectors.toList())
                )
        );
    }
}
