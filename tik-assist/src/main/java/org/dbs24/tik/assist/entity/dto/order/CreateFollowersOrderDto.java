package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;

@Data
public class CreateFollowersOrderDto extends AbstractCreateOrderDto {

    private String tiktokAccountUsername;
}
