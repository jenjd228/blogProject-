package com.blog.spring.repository;

import com.blog.spring.model.Tags;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsRepository extends CrudRepository<Tags,Integer> {

        Tags findTagByName(String name);

        @Query("SELECT e FROM Tags e where e.name like ?1%")
        List<Tags> findTagsByQuery(String query);
}
