package com.blog.spring.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagForTagsDTO {

    String name;

    Double weight;

    public void setWeight(Long postCount, Long postCountWithCurrentTag){
        this.weight = (double) (postCountWithCurrentTag / postCount);
    }

}
