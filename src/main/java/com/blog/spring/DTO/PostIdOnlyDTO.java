package com.blog.spring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostIdOnlyDTO {

    @JsonProperty("post_id")
    private Integer postId;
}
