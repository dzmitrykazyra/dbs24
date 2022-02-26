package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;

@Data
public class OrderPagesQuantityDto {

    private Integer pagesQuantity;

    public static OrderPagesQuantityDto of(Integer pagesQuantity) {

        return StmtProcessor.create(
                OrderPagesQuantityDto.class,
                orderPagesQuantityDto -> orderPagesQuantityDto.setPagesQuantity(pagesQuantity)
        );
    }
}
