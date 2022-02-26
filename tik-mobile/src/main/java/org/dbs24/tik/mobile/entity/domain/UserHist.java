package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tm_users_hist")
@IdClass(UserHistPK.class)
public class UserHist {

    @Id
    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "hash_pass")
    private String hashPass;

    @Column(name = "email")
    private String email;

    @Column(name = "user_status_id")
    private Integer userStatusId;

    @Column(name = "sec_user_id")
    private String secUserId;

    public static UserHist of(User user) {

        return UserHist.builder()
                .userId(user.getId())
                .actualDate(user.getActualDate())
                .userName(user.getUsername())
                .hashPass(user.getHashPass())
                .email(user.getEmail())
                .userStatusId(user.getUserStatus().getUserStatusId())
                .secUserId(user.getSecUserId())
                .build();
    }
}

@Data
class UserHistPK implements Serializable {

    private Integer userId;
    private LocalDateTime actualDate;
}