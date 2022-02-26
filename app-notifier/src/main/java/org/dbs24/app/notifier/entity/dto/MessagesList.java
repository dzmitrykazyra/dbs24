package org.dbs24.app.notifier.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode
public class MessagesList {
    private Collection<MessageDto> messages;
}
