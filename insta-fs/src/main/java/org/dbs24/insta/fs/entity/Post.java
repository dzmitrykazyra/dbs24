/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "ifs_posts")
public class Post extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifs_posts")
    @SequenceGenerator(name = "seq_ifs_posts", sequenceName = "seq_ifs_posts", allocationSize = 1)
    @EqualsAndHashCode.Include
    @NotNull
    @Column(name = "post_id", updatable = false)
    private Long postId;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "post_status_id", referencedColumnName = "post_status_id")
    private PostStatus postStatus;
    
    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "post_type_id", referencedColumnName = "post_type_id")
    private PostType postType;

    
    @NotNull
    @Column(name = "media_id", updatable = false)
    private Long mediaId;

    @NotNull
    @Column(name = "short_code")
    private String shortCode;

}
