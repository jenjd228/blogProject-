package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentDTO {

    private Integer parent_id;

    private Integer post_id;

    private String text;
}
