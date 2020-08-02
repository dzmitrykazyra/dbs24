/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.api;

/**
 *
 * @author kazyra_d
 */
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import java.util.Collection;
import com.kdg.fs24.application.core.nullsafe.NullSafe;

public final class ReferencesCollection<T extends AbstractRefRecord> {

    private Collection<T> refCollection;
    final private String referenceClassName;
    private int hashCodeSum = 0;

    public ReferencesCollection() {
        super();
        this.referenceClassName = "unknown collection";
    }

    public ReferencesCollection(final String referenceClassName) {
        super();
        this.referenceClassName = referenceClassName;
    }

    //==========================================================================
    public final Collection<T> fillRefCollection(AbsReference absReference) {

        if (NullSafe.isNull(this.refCollection)) {
            this.refCollection = ServiceFuncs.<T>createCollection();
        }
        NullSafe.create(this.referenceClassName)
                .execute(() -> {
                    absReference.getReference(this.refCollection);
                });

        return this.refCollection;
    }

    //==========================================================================
    public final int getReferenceHashCode() {

        hashCodeSum = 0;

        refCollection
                .stream()
                .unordered()
                .forEach((record) -> {
                    hashCodeSum += record.calcRecordHash();
                });

        return hashCodeSum;

    }
}
