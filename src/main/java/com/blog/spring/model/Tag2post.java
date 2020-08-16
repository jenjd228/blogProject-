package com.blog.spring.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Tag2post {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(name = "post_id")
    private Integer postId;

    @NotNull
    @Column(name = "tag_id")
    private Integer tagId;
}
