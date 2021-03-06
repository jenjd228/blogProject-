package com.blog.spring.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "global_settings",uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
public class GlobalSettings {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private String value;

}
