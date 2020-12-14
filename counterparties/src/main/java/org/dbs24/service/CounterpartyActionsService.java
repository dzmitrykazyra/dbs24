package org.dbs24.service;

import org.dbs24.entity.core.api.EntityClassesPackages;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.Counterparty;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.references.api.AbstractRefRecord;
import static org.dbs24.consts.SysConst.*;
import java.time.LocalDateTime;

@Data
@Service
@EntityClassesPackages(pkgList = {ENTITY_PACKAGE})
public class CounterpartyActionsService extends AbstractActionExecutionService {

    public Counterparty createCounterparty(String counterpartyCode,
            final String shortName,
            final String fullName) {

        return this.<Counterparty>createActionEntity(Counterparty.class,
                counterparty -> {
                    counterparty.setCounterpartyCode(counterpartyCode);
                    counterparty.setCreationDate(LocalDateTime.now());
                    counterparty.setShortName(shortName);
                    counterparty.setFullName(fullName);
                    counterparty.setEntityStatus(AbstractRefRecord.<EntityStatus>getRefeenceRecord(
                            EntityStatus.class,
                            record -> record.getEntityStatusId().equals(1) && record.getEntityTypeId().equals(200)));
                });
    }
}
