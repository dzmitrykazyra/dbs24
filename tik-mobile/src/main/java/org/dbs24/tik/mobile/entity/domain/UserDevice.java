package org.dbs24.tik.mobile.entity.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "tm_user_devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_user_devices")
    @SequenceGenerator(name = "seq_tm_user_devices", sequenceName = "seq_tm_user_devices", allocationSize = 1)
    @NotNull
    @Column(name = "device_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer deviceId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "device_type_id")
    private DeviceType deviceType;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_version")
    private String appVersion;
}