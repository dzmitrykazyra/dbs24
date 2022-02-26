package org.dbs24.email.spammer.entity.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "spm_spammer")
public class Spammer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_spm_spammers")
    @SequenceGenerator(name = "seq_spm_spammers", sequenceName = "seq_spm_spammers", allocationSize = 1)
    @EqualsAndHashCode.Include
    @NotNull
    @Column(name = "spammer_id")
    private Integer spammerId;

    @Column(name = "spammer_email")
    @NotNull
    private String spammerEmail;

    @Column(name = "is_active")
    @NotNull
    private Boolean isActive;

    @Column(name = "password")
    @NotNull
    private String password;
}
