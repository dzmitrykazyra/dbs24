package org.dbs24.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.test.context.TestPropertySource;

@Log4j2
@TestPropertySource(properties = {
        "config.security.profile.webfilter.chain=development",
        "config.wa.contract.deprecated.update=2147483648",
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false",
        "spring.datasource.url=jdbc:postgresql://193.178.170.145:5432/wa_monitoring",
        "spring.datasource.username=wa_admin",
        "spring.datasource.password=$wa$m4n0t1r9ng$"})
public abstract class AbstractGrpcTest extends AbstractWebTest {
}
