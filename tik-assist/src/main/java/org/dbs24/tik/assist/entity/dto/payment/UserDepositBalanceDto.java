package org.dbs24.tik.assist.entity.dto.payment;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserDeposit;

import java.math.BigDecimal;

@Data
public class UserDepositBalanceDto {

    private BigDecimal actualUserDepositBalance;

    public static UserDepositBalanceDto of(UserDeposit userDeposit) {

        return StmtProcessor.create(
                UserDepositBalanceDto.class,
                userDepositBalanceDto -> userDepositBalanceDto.setActualUserDepositBalance(userDeposit.getRestSum())
        );
    }
}
