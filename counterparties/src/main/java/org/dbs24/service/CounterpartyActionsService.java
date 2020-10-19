package org.dbs24.service;

import org.dbs24.entity.core.api.EntityClassesPackages;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.counterparties.api.Counterparty;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.references.api.AbstractRefRecord;
import java.time.LocalDateTime;

@Data
@Service
@EntityClassesPackages(pkgList = {"org.dbs24.entity.counterparties.api"})
public class CounterpartyActionsService extends AbstractActionExecutionService {

    public Counterparty createCounterparty(final String counterpartyCode,
            final String shortName,
            final String fullName) {

        return this.<Counterparty>createActionEntity(Counterparty.class,
                counterparty -> {
                    counterparty.setCounterpartyCode(counterpartyCode);
                    counterparty.setCreation_date(LocalDateTime.now());
                    counterparty.setShortName(shortName);
                    counterparty.setFullName(fullName);
                    counterparty.setEntityStatus(AbstractRefRecord.<EntityStatus>getRefeenceRecord(
                            EntityStatus.class,
                            record -> record.getEntityStatusId().equals(1)
                            && record.getEntityTypeId().equals(200)));
                });
    }
}
