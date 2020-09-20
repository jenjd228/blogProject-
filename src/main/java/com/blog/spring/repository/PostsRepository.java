package com.blog.spring.repository;

import com.blog.spring.model.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PostsRepository extends CrudRepository<Posts,Integer> {
    @Query("select e from Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED'")
    List<Posts> findBy(Pageable pageable);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 ORDER BY SIZE(e.likeVotes) DESC")
    List<Posts> findByLikeVotes(Pageable pageable , LocalDateTime time);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 ORDER BY SIZE(e.commentCount) DESC")
    List<Posts> findByCommentCount(Pageable pageable , LocalDateTime time);

    Optional<Posts> findById(Integer id);

    //List<Posts> findAllOrderByCreatedOnAsc();

    //Posts findByText(String text);
}
