package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.BotStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum BotStatusDefine {

    BS_BANNED(-1, "Banned"),
    BS_TRACKING(1, "Tracking"),
    BS_EXPIRED(2, "Expired");

    private final Integer id;
    private final String botStatusValue;

    public static List<BotStatus> getAll() {
        return Arrays.stream(BotStatusDefine.values()).map(
                botStatusEnum -> StmtProcessor.create(
                        BotStatus.class,
                        botStatus -> {
                            botStatus.setBotStatusId(botStatusEnum.getId());
                            botStatus.setBotStatusName(botStatusEnum.getBotStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}