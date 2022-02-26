package org.dbs24.tik.mobile.entity.dto.proportion.heart;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.HeartPrice;

import java.math.BigDecimal;

@Data
public class HeartPriceDto {

    private BigDecimal price;
    private Integer heartsAmount;
    private Integer heartsAdditional;

    public static HeartPriceDto of(HeartPrice heartPrice) {

        return StmtProcessor.create(
                HeartPriceDto.class,
                heartPriceDto -> {
                    heartPriceDto.setPrice(heartPrice.getPrice());
                    heartPriceDto.setHeartsAmount(heartPrice.getHeartsAmount());
                    heartPriceDto.setHeartsAdditional(heartPrice.getHeartAdditional());
                }
        );
    }

    public HeartPrice toDefault() {

        return StmtProcessor.create(
                HeartPrice.class,
                heartPrice -> {
                    heartPrice.setPrice(this.getPrice());
                    heartPrice.setHeartsAmount(this.getHeartsAmount());
                    heartPrice.setHeartAdditional(this.getHeartsAdditional());
                }
        );
    }
}
