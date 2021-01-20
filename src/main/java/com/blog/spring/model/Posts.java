package com.blog.spring.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Posts implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "is_active")
    private Integer isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status",nullable = false, columnDefinition =  "enum('NEW','ACCEPTED','DECLINED')")
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    private Long time;

    @NotNull
    private String title;

    @NotNull
    @Lob
    private String text;

    @NotNull
    @Column(name = "view_count")
    private Integer viewCount;

    @NotNull
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    @ManyToOne(optional=false,cascade = CascadeType.ALL)
    private Users user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id", insertable=false, updatable=false)
    @Where(clause = "value = -1")
    private List<PostVotes> dislikeVotes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id", insertable=false, updatable=false)
    @Where(clause = "value = 1")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<PostVotes> likeVotes;

    @OneToMany()
    @JoinColumn(name = "post_id")
    private List<PostComments> commentCount;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = "tag_2_post",
            joinColumns = @JoinColumn(name = "post_id",insertable = false, updatable = false, nullable=false),
            inverseJoinColumns = @JoinColumn(name = "tag_id",insertable = false, updatable = false, nullable=false)
    )
    private List<Tags> tags;

}
