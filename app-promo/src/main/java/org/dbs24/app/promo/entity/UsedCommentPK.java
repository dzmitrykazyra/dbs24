package org.dbs24.app.promo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class UsedCommentPK implements Serializable {

    private Integer orderAction;
    private Integer comment;
}
