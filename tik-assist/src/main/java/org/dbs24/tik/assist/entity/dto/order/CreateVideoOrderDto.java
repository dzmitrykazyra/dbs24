package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;

@Data
public class CreateVideoOrderDto extends AbstractCreateOrderDto {

    private String videoLink;
}
