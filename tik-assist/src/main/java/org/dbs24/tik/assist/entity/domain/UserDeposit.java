package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_user_deposits")
public class UserDeposit {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "currency_iso")
    private String currencyIso;

    @NotNull
    @Column(name = "rest_sum")
    private BigDecimal restSum;
}
