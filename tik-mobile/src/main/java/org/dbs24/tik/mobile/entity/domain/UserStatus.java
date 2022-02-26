package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tm_user_statuses_ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatus {

    @Id
    @Column(name = "user_status_id")
    @NotNull
    private Integer userStatusId;

    @Column(name = "user_status_name")
    private String userStatusName;
}