package com.blog.spring.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag_2_post")
public class Tag2post implements Serializable {

    @EmbeddedId
    private Tag2PostKey key;

    @Embeddable
    @Data
    public static class Tag2PostKey implements Serializable {
        @NotNull
        @Column(name = "post_id")
        private Integer postId;

        @NotNull
        @Column(name = "tag_id")
        private Integer tagId;
    }
}
