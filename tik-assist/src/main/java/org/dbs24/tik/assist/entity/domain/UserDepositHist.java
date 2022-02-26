package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserDepositHistPK.class)
@Table(name = "tik_user_deposits_hist")
public class UserDepositHist {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @Column(name = "currency_iso")
    private String currencyIso;

    @Id
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "rest_sum")
    private BigDecimal restSum;

    public static UserDepositHist of(UserDeposit userDeposit) {

        return UserDepositHist.builder()
                .userId(userDeposit.getUserId())
                .actualDate(userDeposit.getActualDate())
                .restSum(userDeposit.getRestSum())
                .currencyIso(userDeposit.getCurrencyIso())
                .build();
    }
}

@Data
class UserDepositHistPK implements Serializable {

    private Integer userId;
    private LocalDateTime actualDate;
}