package org.dbs24.tik.mobile.entity.dto.proportion.heart;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.HeartPrice;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class HeartPriceListDto {

    private List<HeartPriceDto> heartPriceDtoList;

    public static HeartPriceListDto of(List<HeartPrice> heartPrices) {

        return StmtProcessor.create(
                HeartPriceListDto.class,
                heartPriceListDto -> heartPriceListDto.setHeartPriceDtoList(
                        heartPrices.stream()
                                .map(HeartPriceDto::of)
                                .collect(Collectors.toList())
                )
        );
    }
}
