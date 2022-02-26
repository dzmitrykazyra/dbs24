package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_users")
    @SequenceGenerator(name = "seq_tm_users", sequenceName = "seq_tm_users", allocationSize = 1)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "hash_pass")
    private String hashPass;

    @Column(name = "user_name")
    private String username;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_status_id")
    private UserStatus userStatus;

    @Column(name = "sec_user_id")
    private String secUserId;
}
