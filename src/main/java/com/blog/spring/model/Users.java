package com.blog.spring.model;

import com.sun.istack.NotNull;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Users {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(name = "is_moderator")
    private Integer isModerator;

    @NotNull
    @Column(name = "reg_time")
    private Date regTime;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String code;

    private String photo;


}
