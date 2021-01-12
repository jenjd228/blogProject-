package com.blog.spring.repository;

import com.blog.spring.model.PostComments;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("checkstyle:WhitespaceAfter")
@Repository
public interface PostCommentsRepository extends CrudRepository<PostComments, Long> {
    @Query("SELECT COUNT(t) FROM PostComments t WHERE t.postId = ?1")
    Integer getTotalCount(Integer postId);

    PostComments findPostCommentsById(Integer id);
}
