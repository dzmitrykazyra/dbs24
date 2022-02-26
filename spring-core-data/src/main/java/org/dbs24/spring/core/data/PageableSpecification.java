package org.dbs24.spring.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface PageableSpecification<T> {
    Page<T> getPageableSpecification(Pageable pageable);
}
