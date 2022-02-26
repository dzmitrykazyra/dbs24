package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.PhoneStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PhoneStatusDefine {

    PS_ACTUAL(1, "PS.ACTUAL"),
    PS_BANNED(2, "PS.BANNED");

    private final Integer id;
    private final String phoneStatusValue;

    public static List<PhoneStatus> getAll() {
        return Arrays.stream(PhoneStatusDefine.values()).map(
                phoneStatusEnum -> StmtProcessor.create(
                        PhoneStatus.class,
                        phoneStatus -> {
                            phoneStatus.setPhoneStatusId(phoneStatusEnum.getId());
                            phoneStatus.setPhoneStatusName(phoneStatusEnum.getPhoneStatusValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}