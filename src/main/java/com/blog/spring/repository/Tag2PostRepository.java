package com.blog.spring.repository;

import com.blog.spring.DTO.TagNameAndWeight;
import com.blog.spring.model.Tag2post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2post, Integer> {

    @Query(value = "select tags.name as name, count(*) as weight from tag_2_post join" +
            " tags on tag_id = tags.id join posts on post_id = posts.id" +
            " where posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED'" +
            " group by tags.id order by weight desc;", nativeQuery = true)
    List<TagNameAndWeight> findTagsAndSortByCountOfPosts();

    @Query(value = "select tags.name as name, count(*) as weight from tag_2_post join" +
            " tags on tag_id = tags.id join posts on post_id = posts.id" +
            " where posts.is_active = 1 and posts.moderation_status = 'ACCEPTED' and tags.name like ?1%" +
            " group by tags.id order by weight desc;", nativeQuery = true)
    List<TagNameAndWeight> findTagsByQueryAndSortByCountOfPosts(String query);
}
