package com.blog.spring.repository;

import com.blog.spring.DTO.Statistics;
import com.blog.spring.model.PostVotes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotersRepository extends CrudRepository<PostVotes, Integer> {
    @Query("SELECT COUNT(t) FROM PostVotes t WHERE t.postId = ?1 AND t.value = ?2")
    Integer getLikeOrDislikeCount(Integer postId, Integer value);

    @Query(value = "select count(*) as likesCount,(select count(*) from post_voters where value = -1) as dislikesCount," +
            " (SELECT COUNT(*) FROM posts) as postsCount," +
            " (SELECT sum(posts.view_count) FROM posts) as viewsCount," +
            " (SELECT min(posts.time) FROM posts) as firstPublication" +
            " from post_voters join posts on post_voters.post_id = posts.id where value = 1;", nativeQuery = true)
    Statistics getStatistics();

    @Query(value = "SELECT count(*) as likesCount,(select COUNT(*) from post_voters where value = -1 and post_voters.post_id in :ids) as dislikesCount, (SELECT COUNT(*) FROM posts where posts.id in :ids) as postsCount, (SELECT sum(posts.view_count) FROM posts where posts.id in :ids) as viewsCount, (SELECT min(posts.time) FROM posts where posts.id in :ids) as firstPublication from post_voters join posts on post_voters.post_id = posts.id where value = 1 and post_voters.post_id in :ids ;", nativeQuery = true)
    Statistics getMyStatistics(@Param("ids") Iterable<Integer> ids);

    @Query(value = "SELECT e FROM PostVotes e where e.postId = ?1 AND e.userId = ?2")
    PostVotes getPostVotesByUserIdAndPostId(Integer postId, Integer userId);
}
