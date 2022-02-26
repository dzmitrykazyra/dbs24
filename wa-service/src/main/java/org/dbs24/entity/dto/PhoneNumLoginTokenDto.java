package org.dbs24.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PhoneNumLoginTokenDto {
    private final String phoneNumber;
    private final String loginToken;
}
