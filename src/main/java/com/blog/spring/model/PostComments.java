package com.blog.spring.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_comments")
public class PostComments {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;

    //@JoinColumn(name = "parent_id")
    //@OneToOne(cascade =  CascadeType.ALL)
    //private PostComments parent;

    @NotNull
    @Column(name = "post_id")
    private Integer postId;

    //@NotNull
    //@JoinColumn(name = "post_id")
    //@ManyToOne(cascade = CascadeType.ALL)
    //private Posts post;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    //@NotNull
    //@JoinColumn(name = "user_id")
    //@ManyToOne(cascade = CascadeType.ALL)
    //private Users user;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private String text;
}
