package com.blog.spring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPostDTO {

    private long timestamp;

    private int active;

    private String title;

    private List<String> tags;

    private String text;
}
