package com.blog.spring.DTO;
import com.blog.spring.model.Posts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagForTagsDTO {

    String name;

    Double weight;

    public void setWeight(Long postCount){
        this.weight = weight / postCount;
    }

    public void setWeight(List<Posts> list){
        this.weight = (double) list.size();
    }

    public void setWeight(Double weight){
        this.weight = weight;
    }

}
