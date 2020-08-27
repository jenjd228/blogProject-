package com.blog.spring.model;

import com.sun.istack.NotNull;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class PostComments {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;

    @NotNull
    @Column(name = "post_id")
    private Integer postId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private String text;
}
