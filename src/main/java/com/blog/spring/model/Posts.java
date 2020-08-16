package com.blog.spring.model;

import com.blog.spring.ModerationStatus;
import com.sun.istack.NotNull;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Posts {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private Integer isActive;

    @NotNull
    private Enum<ModerationStatus> moderation_status;

    private Integer moderatorId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    private Date time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    @Column(name = "view_count")
    private Integer viewCount;

}
