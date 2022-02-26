package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.DepositPaymentType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PaymentTypeDefine {

    PT_SUBSCRIPTION(1, "PT.SUBSCRIPTION"),
    PT_PLAN(2, "PT.PLAN"),
    PT_ORDER(2, "PT.ORDER");

    private final Integer id;
    private final String paymentTypeValue;

    public static List<DepositPaymentType> getAll() {
        return Arrays.stream(PaymentTypeDefine.values()).map(
                paymentTypeEnum -> StmtProcessor.create(
                        DepositPaymentType.class,
                        paymentType -> {
                            paymentType.setPaymentTypeId(paymentTypeEnum.getId());
                            paymentType.setPaymentTypeName(paymentTypeEnum.getPaymentTypeValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}