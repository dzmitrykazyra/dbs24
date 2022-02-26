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
@Table(name = "spm_applications")
public class Application {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "application_title")
    @NotNull
    private String applicationTitle;
}
