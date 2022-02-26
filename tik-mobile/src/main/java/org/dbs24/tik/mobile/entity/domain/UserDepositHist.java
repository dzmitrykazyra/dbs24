package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_user_deposits_hist")
@IdClass(UserDepositHistPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDepositHist {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @Id
    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "rest_sum")
    private Integer restSum;

    public static UserDepositHist toUserDepositHist(UserDeposit userDeposit) {
        return UserDepositHist.builder()
                .withUserId(userDeposit.getUserId())
                .withActualDate(userDeposit.getActualDate())
                .withRestSum(userDeposit.getRestSum())
                .build();
    }
}

@Data
class UserDepositHistPK implements Serializable {
    private Integer userId;
    private LocalDateTime actualDate;
}