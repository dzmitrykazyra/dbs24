package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.CurrencyDao;
import org.dbs24.tik.mobile.dao.HeartPriceDao;
import org.dbs24.tik.mobile.entity.domain.HeartPrice;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.heart.HeartPriceListDto;
import org.dbs24.tik.mobile.service.HeartPriceService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j2
@Component
public class HeartPriceServiceImpl implements HeartPriceService {

    private final HeartPriceDao heartPriceDao;

    private final CurrencyDao currencyDao;

    public HeartPriceServiceImpl(HeartPriceDao heartPriceDao, CurrencyDao currencyDao) {

        this.heartPriceDao = heartPriceDao;
        this.currencyDao = currencyDao;
    }

    @Override
    public Mono<HeartPriceDto> create(Mono<HeartPriceDto> heartPriceToCreateMono) {

        return heartPriceToCreateMono.map(
                heartPriceDto -> {
                    HeartPrice heartPriceToSave = heartPriceDto.toDefault();
                    heartPriceToSave.setCurrency(currencyDao.findUsdCurrency());

                    return HeartPriceDto.of(heartPriceDao.save(heartPriceToSave));
                }
        );
    }

    @Override
    public Mono<HeartPriceDto> getHeartPriceDtoByHeartsAmount(Integer heartsAmount) {

        return Mono.just(
                StmtProcessor.create(
                        HeartPriceDto.class,
                        heartPriceDto -> {
                            heartPriceDto.setPrice(getHeartPriceByHeartsAmount(heartsAmount));
                            heartPriceDto.setHeartsAmount(heartsAmount);
                        }
                )
        );
    }

    @Override
    public BigDecimal getHeartPriceByHeartsAmount(Integer heartsAmount) {

        HeartPrice heartsPrice = heartPriceDao.getByHeartsAmount(currencyDao.findUsdCurrency(), heartsAmount);

        return heartsPrice.getPrice()
                .divide(BigDecimal.valueOf(heartsPrice.getHeartsAmount()), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(heartsAmount));
    }

    @Override
    public Mono<HeartPriceListDto> getHeartPrices() {

        return Mono.just(
                HeartPriceListDto.of(
                        heartPriceDao.getAll()
                )
        );
    }
}
