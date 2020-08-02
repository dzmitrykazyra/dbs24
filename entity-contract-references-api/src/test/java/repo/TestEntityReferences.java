/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import com.kdg.fs24.application.core.service.funcs.TestFuncs;
import com.kdg.fs24.persistence.core.PersistanceEntityManager;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import config.TestReferencesConfig;
import com.kdg.fs24.service.EntityContractReferencesService;
import org.junit.Test;
import com.kdg.fs24.entity.contract.subjects.ContractSubject;

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
    private PersistanceEntityManager persistanceEntityManager;

    @Autowired
    private EntityContractReferencesService entityContractReferencesService;

    final private Integer entityContractSubjectId = 999;

    @Test
    public void testTypeAndStatuses() {
        //this.initializeTest();

        String testString = TestFuncs.generateTestString20();

        entityContractReferencesService.createContractSubject(entityContractSubjectId, testString);

        persistanceEntityManager
                .getEntityManager()
                .remove(
                        persistanceEntityManager
                                .getEntityManager()
                                .find(ContractSubject.class, entityContractSubjectId));

    }
}
