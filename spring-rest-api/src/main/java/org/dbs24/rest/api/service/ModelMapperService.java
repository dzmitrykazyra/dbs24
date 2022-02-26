package org.dbs24.rest.api.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.locale.NLS.long2LocalDateTime;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.nvl;


@Getter
@Log4j2
@EqualsAndHashCode(callSuper = true)
@Service
public class ModelMapperService extends AbstractApplicationService {

    @Bean
    public ModelMapper modelMapper() {

        return create(ModelMapper.class, modelMapper -> {
            // LocalDateTime
            modelMapper.addConverter(ctx -> localDateTime2long(ctx.getSource()), LocalDateTime.class, Long.class);
            modelMapper.addConverter(ctx -> nvl(long2LocalDateTime(ctx.getSource()), now()), Long.class, LocalDateTime.class);

        });
    }
}
