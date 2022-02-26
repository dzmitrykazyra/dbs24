package org.dbs24.rest.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;

@Log4j2
public class QueryMetrics {

    final AtomicInteger qryCounter = new AtomicInteger();

    public QueryMetrics() {
        qryCounter.set(0);
    }

    @Getter
    @Builder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
    private static class QueryMetric {
        @EqualsAndHashCode.Include
        Integer seq;
        StopWatcher stopWatcher;
        String execMsg;
    }

    final Collection<QueryMetric> queries = createConcurencyCollection();

    public Integer registryQuery(String methodName, String path) {

        final Integer num = qryCounter.incrementAndGet();

        queries.add(QueryMetric.builder()
                .seq(num)
                .stopWatcher(StopWatcher.create(methodName.concat(path)))
                .build()
        );

        return num;

    }

    public void finishQuery(Integer num, String signal) {

        queries.stream().filter(q -> q.getSeq().equals(num))
                .findFirst()
                .ifPresentOrElse(query -> {

                    log.debug("{}: {}", signal, query.getStopWatcher().getStringExecutionTime());

                    queries.remove(query);

                }, () -> log.warn("Unknown query or removed: {}", num));
    }

    public void filterExecuted() {

        queries.forEach(qry -> {

            if ((qry.getStopWatcher().getExecutionTime() > 5000)) {

                log.warn("bad on broken query: {} ".toUpperCase(), qry.getStopWatcher().getStringExecutionTime());
                queries.remove(qry);
            }
        });
    }
}
