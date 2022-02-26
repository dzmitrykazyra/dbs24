package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.BotRegistrationType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum BotRegistrationTypeDefine {

    BRT_SMS(1, "SMS"),
    BRT_EMAIL(2, "Email"),
    BRT_GOOGLE(3, "Google service");

    private final Integer id;
    private final String botRegistryTypeValue;

    public static List<BotRegistrationType> getAll() {
        return Arrays.stream(BotRegistrationTypeDefine.values()).map(
                botRegistryTypeEnum -> StmtProcessor.create(
                        BotRegistrationType.class,
                        botRegistryType -> {
                            botRegistryType.setBotRegistryTypeId(botRegistryTypeEnum.getId());
                            botRegistryType.setBotRegistryTypeName(botRegistryTypeEnum.getBotRegistryTypeValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}
