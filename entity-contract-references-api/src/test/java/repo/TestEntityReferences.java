/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.component.PersistenceEntityManager;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import config.TestReferencesConfig;
import org.dbs24.service.EntityContractReferencesService;
import org.junit.Test;
import org.dbs24.entity.ContractSubject;

/**
 *
 * @author Козыро Дмитрий
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestReferencesConfig.class)
//@DataJpaTest
@Data
public class TestEntityReferences {

    @Autowired
    private PersistenceEntityManager PersistenceEntityManager;

    @Autowired
    private EntityContractReferencesService entityContractReferencesService;

    final private Integer entityContractSubjectId = 999;

    @Test
    public void testTypeAndStatuses() {
        //this.initializeTest();

        String testString = TestFuncs.generateTestString20();

        entityContractReferencesService.createContractSubject(entityContractSubjectId, testString);

        PersistenceEntityManager
                .getEntityManager()
                .remove(
                        PersistenceEntityManager
                                .getEntityManager()
                                .find(ContractSubject.class, entityContractSubjectId));

    }
}
