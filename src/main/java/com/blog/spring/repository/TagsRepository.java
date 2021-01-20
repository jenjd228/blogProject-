package com.blog.spring.repository;

import com.blog.spring.model.Tags;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TagsRepository extends CrudRepository<Tags,Integer> {

        Tags findTagByName(String name);

        List<Tags> findTagsByNameContaining(String query);

        List<Tags> findTagsIdByNameIn(List<String> tags);

        @Transactional
        @Modifying
        @Query(value = "insert ignore into tags(name) values (?1)", nativeQuery = true)
        void saveIgnoreDuplicateKey(String name);
}
