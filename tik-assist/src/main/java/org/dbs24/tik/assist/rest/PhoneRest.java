/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.dbs24.tik.assist.constant.RequestQueryParam;
import org.dbs24.tik.assist.entity.dto.phone.CreatedPhoneDto;
import org.dbs24.tik.assist.entity.dto.phone.CreatedPhoneUsageDto;
import org.dbs24.tik.assist.entity.dto.phone.PhoneDto;
import org.dbs24.tik.assist.entity.dto.phone.PhoneUsageDto;
import org.dbs24.tik.assist.service.phone.PhonesService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "tik-assist")
public class PhoneRest extends ReactiveRestProcessor {

    final PhonesService phonesService;

    public PhoneRest(PhonesService phonesService) {

        this.phonesService = phonesService;
    }

    public Mono<ServerResponse> createOrUpdatePhone(ServerRequest request) {

        return this.<PhoneDto, CreatedPhoneDto>createResponse(
                request,
                PhoneDto.class,
                CreatedPhoneDto.class,
                phonesService::createOrUpdatePhone);
    }

    public Mono<ServerResponse> createOrUpdatePhoneUsage(ServerRequest request) {

        return this.<PhoneUsageDto, CreatedPhoneUsageDto>createResponse(
                request,
                PhoneUsageDto.class,
                CreatedPhoneUsageDto.class,
                phonesService::createOrUpdatePhoneUsage);
    }

    public Mono<ServerResponse> getPhone(ServerRequest request) {

        return this.<PhoneDto>createResponse(
                request,
                PhoneDto.class,
                () -> phonesService.getPhone(getIntegerFromParam(request, RequestQueryParam.QP_PHONE_ID)));
    }    

    public Mono<ServerResponse> getLongestNotUsedPhone(ServerRequest request) {

        return this.<PhoneDto>createResponse(
                request,
                PhoneDto.class,
                phonesService::getLongestNotUsedPhone);
    }       
}
