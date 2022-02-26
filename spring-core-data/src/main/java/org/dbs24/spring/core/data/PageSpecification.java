package org.dbs24.spring.core.data;

import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface PageSpecification<T> {
    Specification<T> getQuerySpecification();
}
