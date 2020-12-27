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

    private String name;

    private Double weight;

    public void setNormalWeight(Long postCount){
        this.weight = weight / postCount;
    }

    public void setWeightLikePostCount(Object count){
        this.weight = Double.valueOf(count.toString());
    }

    public void setNameLikeObject(Object name){
        this.name = name.toString();
    }

    public void setWeight(Double weight){
        this.weight = weight;
    }

}
