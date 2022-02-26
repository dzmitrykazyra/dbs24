package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_user_deposits")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDeposit {

    @Id
    @NotNull
    @Column(name = "user_id", updatable = false)
    private Integer userId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "rest_sum")
    private Integer restSum;
}
