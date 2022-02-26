package org.dbs24.tik.assist.service;

import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.tik.assist.entity.dto.Dto;

public interface DtoDomainTransferable<DTO extends Dto, Domain extends PersistenceEntity> {

    Domain transfer(DTO dto);
}
