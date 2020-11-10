package com.blog.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tags {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @OneToMany
    @JoinColumn(name = "tag_id")
    private List<Tag2post> tag2posts;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Posts> posts;

}
