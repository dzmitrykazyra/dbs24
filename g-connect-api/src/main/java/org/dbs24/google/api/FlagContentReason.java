package org.dbs24.google.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlagContentReason {
    SEXUAL_CONTENT(1),
    GRAPHIC_VIOLENCE(3),
    HATEFUL_OR_ABUSIVE_CONTENT(4),
    IMPROPER_CONTENT_RATING(5),
    HARMFUL_TO_DEVICE_OR_DATA(7),
    ILLEGAL_PRESRIPTION_OR_OTHER_DRUG(11),
    COPYCAT_OR_IMPERSONATION(12),
    OTHER_OBJECTION(8);

    public int value;
}
