package com.blog.spring.repository;

import com.blog.spring.model.Tags;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends CrudRepository<Tags,Integer> {
        Tags findTagByName(String name);
}
