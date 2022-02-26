package org.dbs24.app.notifier.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Embeddable
public class MessageDetails {

    @EqualsAndHashCode.Include
    @Column(name = "msg_header")
    private String msgHeader;
    @Column(name = "msg_body")
    private String msgBody;
}
