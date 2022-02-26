package org.dbs24.auth.server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class ServerHistPK implements Serializable {

    private Integer serverId;
    private LocalDateTime actualDate;

}