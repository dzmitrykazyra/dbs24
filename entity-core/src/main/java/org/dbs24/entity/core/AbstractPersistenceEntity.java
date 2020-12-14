/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core;

import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;
import static org.dbs24.consts.SysConst.DATE_FORMAT;
import org.dbs24.entity.core.api.ActionEntity;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.stmt.StmtProcessor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Entity
@Table(name = "core_Entities")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class AbstractPersistenceEntity implements ActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_action_id")
    @SequenceGenerator(name = "seq_action_id", sequenceName = "seq_action_id", allocationSize = 1)
    @Column(name = "entity_id", updatable = false)
    private Long entity_id;
    //--------------------------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "entity_status_id", referencedColumnName = "entity_status_id"),
        @JoinColumn(name = "entity_type_id", referencedColumnName = "entity_type_id")})
    @NotNull
    private EntityStatus entityStatus;
    //--------------------------------------------------------------------------
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;
    //--------------------------------------------------------------------------
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "close_date")
    private LocalDate closeDate;
    //--------------------------------------------------------------------------
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "last_modify")
    private LocalDateTime lastModify;

    //--------------------------------------------------------------------------
    @Override
    public Boolean justCreated() {
        return StmtProcessor.isNull(this.lastModify);
    }
    
    @Override
    public Long entityId() {
        return (this.entity_id);
    }
    //--------------------------------------------------------------------------
    @JsonIgnore
    @Transient
    private Map<String, Object> metaData;
}
