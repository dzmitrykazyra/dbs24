package org.dbs24.proxy.core.dao;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.proxy.core.repo.ApplicationNetworkRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class ApplicationNetworkDao {

    final ApplicationNetworkRepo applicationNetworkRepo;

    public ApplicationNetworkDao(ApplicationNetworkRepo applicationNetworkRepo) {
        this.applicationNetworkRepo = applicationNetworkRepo;
    }

    public Optional<ApplicationNetwork> findApplicationNetworkOptionalByName(String applicationNetworkName) {

        return applicationNetworkRepo
                .findByApplicationNetworkName(applicationNetworkName);
    }

    public List<ApplicationNetwork> findAllApplicationNetworks() {

        return applicationNetworkRepo.findAll();
    }

    public ApplicationNetwork findApplicationNetworkByName(String applicationNetworkName) {

        return applicationNetworkRepo
                .findByApplicationNetworkName(applicationNetworkName)
                .orElseThrow(() -> new RuntimeException("No such network"));
    }

    public ApplicationNetwork save(ApplicationNetwork applicationNetwork) {

        return applicationNetworkRepo.save(applicationNetwork);
    }
}
