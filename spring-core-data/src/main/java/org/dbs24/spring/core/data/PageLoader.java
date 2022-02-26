/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.data;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.GenericFuncs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.dbs24.consts.SysConst.INTEGER_ONE;
import static org.dbs24.consts.SysConst.INTEGER_ZERO;

@Log4j2
public abstract class PageLoader<T> {

    private Integer pageSize = 100;
    private PageSpecification<T> pageSpecification;
    private PageableSpecification<T> pageableSpecification;
    private JpaSpecificationExecutor jpaSpecificationExecutor;
    private AtomicInteger itemsCount = new AtomicInteger(0);

    protected void addPage(Collection<T> pageOrders) {
        throw new RuntimeException("addPage method implementation required");
    }


    public Page<T> processPage(int i) {

        final Pageable pageable = PageRequest.of(i, pageSize);
        final Page<T> page = jpaSpecificationExecutor.findAll(pageSpecification.getQuerySpecification(), pageable);

        final Collection<T> items = page.getContent();

        itemsCount.addAndGet(items.size());

        addPage(items);

        return page;
    }

    public Page<T> processPageable(int i) {

        final Pageable pageable = PageRequest.of(i, pageSize);
        final Page<T> page = pageableSpecification.getPageableSpecification(pageable);

        final Collection<T> items = page.getContent();

        itemsCount.addAndGet(items.size());

        addPage(items);

        return page;
    }

    //==========================================================================
    public void loadRecords(JpaSpecificationExecutor jpaSpecificationExecutor, PageSpecification<T> pageSpecification) {
        loadRecords(jpaSpecificationExecutor, pageSpecification, 100);
    }

    public void loadRecords(JpaSpecificationExecutor jpaSpecificationExecutor, PageSpecification<T> pageSpecification, Integer pageSze) {
        this.pageSize = pageSze;
        this.jpaSpecificationExecutor = jpaSpecificationExecutor;
        this.pageSpecification = pageSpecification;

        final var genClassNane = GenericFuncs.getTypeParameterClass(getClass()).getSimpleName();

        final var stopWatcher = StopWatcher.create();

        final var startPage = processPage(INTEGER_ZERO);

        final var pgAmt = startPage.getTotalPages();

        log.debug("{}: there is/are {} page(s) ", genClassNane, pgAmt);

        if (pgAmt > INTEGER_ONE) {
            IntStream.range(INTEGER_ONE, pgAmt)
                    .parallel()
                    .forEach(this::processPage);
        }

        log.debug("{}: successfully process {} page(s), {} record(s), {}",
                genClassNane, pgAmt, itemsCount.get(), stopWatcher.getStringExecutionTime());
    }
    //==================================================================================================================
    public void loadRecords(PageableSpecification<T> pageableSpecification, Integer pageSze) {
        this.pageSize = pageSze;
        this.pageableSpecification = pageableSpecification;

        final var genClassNane = GenericFuncs.getTypeParameterClass(getClass()).getSimpleName();

        final var stopWatcher = StopWatcher.create();

        final var startPage = processPageable(INTEGER_ZERO);

        final var pgAmt = startPage.getTotalPages();

        log.debug("{}: there is/are {} page(s) ", genClassNane, pgAmt);

        if (pgAmt > INTEGER_ONE) {
            IntStream.range(INTEGER_ONE, pgAmt)
                    .parallel()
                    .forEach(this::processPageable);
        }

        log.debug("{}: successfully process {} page(s), {} record(s), {}",
                genClassNane, pgAmt, itemsCount.get(), stopWatcher.getStringExecutionTime());
    }
}
