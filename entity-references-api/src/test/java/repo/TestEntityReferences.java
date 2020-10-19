package repo;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.entity.type.EntityType;
import org.dbs24.entity.kind.EntityKind;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.entity.action.ActionCode;
import org.dbs24.persistence.core.PersistenceEntityManager;
import java.time.LocalDateTime;
import org.hibernate.Session;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import org.springframework.context.annotation.Import;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.repository.*;
import org.dbs24.service.EntityReferencesService;
import org.dbs24.repository.EntityStatusesRepository;
import lombok.Data;
import static org.dbs24.application.core.sysconst.SysConst.*;
import config.TestRepoConfig;
import org.dbs24.entity.status.EntityStatusPK;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Козыро Дмитрий
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestRepoConfig.class)
//@DataJpaTest
@Data
public class TestEntityReferences {

//public class TestRepositories extends Unit4Test<TestSpringBoot, TestRepoConfig> {
    @Autowired
    private PersistenceEntityManager PersistenceEntityManager;

//    @Autowired
//    private EntityTypesRepository entityTypesRepository;
//
//    @Autowired
//    private EntityStatusesRepository entityStatusesRepository;
    @Autowired
    private EntityReferencesService entityReferencesService;

    final private Integer entityStatusId4Test = 999;
    final private Integer entityType4Test = 999;
    final private Integer entityKind4Test = 999;
    final private Integer entityActionCode4Test = 999;

//
//    @Autowired(required = false)
//    private EntityStatusesRepository entityStatusesRepository;
    @Test
    public void testTypeAndStatuses() {
        //this.initializeTest();

        String testString = TestFuncs.generateTestString20();

        entityReferencesService.createNewEntityType(entityType4Test, testString, testString);

        testString = TestFuncs.generateTestString20();

        entityReferencesService.createNewEntityKind(entityKind4Test, entityType4Test, testString);

        testString = TestFuncs.generateTestString20();
        entityReferencesService.createNewEntityStatus(entityStatusId4Test, entityType4Test, testString);

        final EntityStatusPK entityStatusPK = NullSafe.createObject(EntityStatusPK.class);

        entityStatusPK.setEntityStatusId(entityStatusId4Test);
        entityStatusPK.setEntityTypeId(entityType4Test);

        final EntityStatus entityStatus = PersistenceEntityManager
                .getEntityManager()
                .find(EntityStatus.class, entityStatusPK);

        PersistenceEntityManager
                .getEntityManager()
                .remove(entityStatus);

        PersistenceEntityManager
                .getEntityManager().remove(PersistenceEntityManager
                        .getEntityManager().find(EntityKind.class, entityKind4Test));
        PersistenceEntityManager
                .getEntityManager().remove(PersistenceEntityManager
                        .getEntityManager().find(EntityType.class, entityType4Test));

        //this.printAllRepositories();
    }

    @Test
    public void testActionCodes() {
        //this.initializeTest();

        final String testString = TestFuncs.generateTestString20();

        entityReferencesService.createNewActionCode(entityActionCode4Test, testString, testString, Boolean.FALSE);

        PersistenceEntityManager.getEntityManager().remove(PersistenceEntityManager.getEntityManager().find(ActionCode.class,
                entityActionCode4Test));

    }
}
