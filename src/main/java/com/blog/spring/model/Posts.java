package com.blog.spring.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(name = "is_active")
    private Integer isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status",nullable = false, columnDefinition =  "enum('NEW','ACCEPTED','DECLINED')")
    private ModerationStatus moderationStatus;

    //@JoinColumn(name = "moderator")
    //@ManyToOne(cascade = CascadeType.ALL)
    //private Users moderator;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    @ManyToOne(optional=false,cascade = CascadeType.ALL)
    private Users user;

    @NotNull
    private Long time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    @Column(name = "view_count")
    private Integer viewCount;

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

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "tag_2_post",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tags> tags ;

}
