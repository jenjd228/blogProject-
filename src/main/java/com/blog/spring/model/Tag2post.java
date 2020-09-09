package com.blog.spring.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "tag_2_post")
public class Tag2post {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(name = "post_id")
    //@JoinColumn(name = "post_id")
    //@ManyToMany(cascade = CascadeType.ALL)
    private Integer postId;

    @NotNull
    @Column(name = "tag_id")
    //@JoinColumn(name = "tag")
    //@ManyToMany(cascade = CascadeType.ALL)
    private Integer tagId;
}
