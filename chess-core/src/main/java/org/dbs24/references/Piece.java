/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.api.ReferenceRec;
import java.util.Collection;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Arrays;

@Data
@Entity
@Table(name = "wc_PiecesRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Piece extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "piece_code")
    private String pieceCode;
    @Column(name = "piece_name")
    private String pieceName;
}
