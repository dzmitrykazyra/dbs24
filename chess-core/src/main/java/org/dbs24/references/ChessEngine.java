/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references;

import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Table(name = "chess_EnginesRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChessEngine extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "engine_id")
    private Integer engineId;
    @Column(name = "engine_name")
    private String engineName;
}
