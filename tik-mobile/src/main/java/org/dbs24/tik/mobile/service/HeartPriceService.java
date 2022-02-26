package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceListDto;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface HeartPriceService {

    Mono<HeartPriceDto> create(Mono<HeartPriceDto> heartPriceToCreate);

    Mono<HeartPriceDto> getHeartPriceDtoByHeartsAmount(Integer heartsAmount);

    BigDecimal getHeartPriceByHeartsAmount(Integer heartsAmount);

    Mono<HeartPriceListDto> getHeartPrices();
}
