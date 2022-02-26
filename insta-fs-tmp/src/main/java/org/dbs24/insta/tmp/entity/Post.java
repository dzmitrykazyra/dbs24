/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "POSTS")
public class Post extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_posts")
    @SequenceGenerator(name = "seq_posts", sequenceName = "seq_posts", allocationSize = 1)
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
    @Column(name = "insta_post_id", updatable = false)
    private Long instaPostId;    
    
    @NotNull
    @Column(name = "media_id", updatable = false)
    private Long mediaId;

    @NotNull
    @Column(name = "short_code")
    private String shortCode;
}
