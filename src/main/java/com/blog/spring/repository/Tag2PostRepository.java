package com.blog.spring.repository;

import java.util.List;

import com.blog.spring.DTO.TagNameAndWeight;
import com.blog.spring.model.Tag2post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2post,Integer> {

    @Query(value = "SELECT tags.name as name, COUNT(*) as weight FROM tag_2_post JOIN" +
            " tags ON tag_id = tags.id JOIN posts ON post_id = posts.id" +
            " WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED'" +
            " GROUP BY tags.id ORDER BY weight DESC;", nativeQuery = true)
    List<TagNameAndWeight> findTagsAndSortByCountOfPosts();

    @Query(value = "SELECT tags.name as name, COUNT(*) as weight FROM tag_2_post JOIN" +
            " tags ON tag_id = tags.id JOIN posts ON post_id = posts.id" +
            " WHERE posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND tags.name like ?1%" +
            " GROUP BY tags.id ORDER BY weight DESC;", nativeQuery = true)
    List<TagNameAndWeight> findTagsByQueryAndSortByCountOfPosts(String query);
}
