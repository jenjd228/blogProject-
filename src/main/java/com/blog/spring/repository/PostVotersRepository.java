package com.blog.spring.repository;

import com.blog.spring.model.PostVotes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotersRepository extends CrudRepository<PostVotes,Integer> {
    @Query("SELECT COUNT(t) FROM PostVotes t WHERE t.postId = ?1 AND t.value = ?2")
    Integer getLikeOrDislikeCount(Integer postId,Integer value);
}
