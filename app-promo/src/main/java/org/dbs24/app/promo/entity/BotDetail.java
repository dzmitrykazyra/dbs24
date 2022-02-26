package org.dbs24.app.promo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "pr_bot_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotDetail implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "bot_id")
    private Integer botId;

    @NotNull
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "phone_config_id")
    private PhoneConfig phoneConfig;

    @Column(name = "ya29_gmail_token")
    private String ya29GmailToken;

    @Column(name = "ya29_gplay_token")
    private String ya29GPlayToken;

    @Column(name = "aas_token")
    private String aasToken;

    @Column(name = "gsf_id")
    private String gsfId;
}
