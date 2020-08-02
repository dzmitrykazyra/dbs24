/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import config.CounterpartiesTestConfig;
import java.util.UUID;
import org.dbs24.entity.counterparties.api.Counterparty;
import org.dbs24.entity.counterparties.api.CounterpartyConst;
import org.dbs24.service.CounterpartyActionsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.dbs24.entity.status.EntityStatus;

/**
 *
 * @author Козыро Дмитрий
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(CounterpartiesTestConfig.class)
//@DataJpaTest
@Data
public class MainTest {

    @Autowired
    private CounterpartyActionsService counterpartyActionsService;

    @Test
    public void test1() {
        final String testValue = UUID.randomUUID().toString().substring(1, 20);

        //==========================================================
        final Counterparty counterparty = counterpartyActionsService
                .createCounterparty(testValue, testValue, testValue);

        //entityManager.persist(user);
        // создание пользователя через действие
        counterpartyActionsService.executeAction(counterparty, CounterpartyConst.ACT_CREATE_OR_MODIFY_COUNTERPARTY, null);
    }
}
