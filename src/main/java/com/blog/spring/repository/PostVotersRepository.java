package com.blog.spring.repository;

import com.blog.spring.DTO.Statistics;
import com.blog.spring.model.PostVotes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotersRepository extends CrudRepository<PostVotes, Integer> {
    @Query("select count(t) from PostVotes t where t.postId = ?1 and t.value = ?2")
    Integer getLikeOrDislikeCount(Integer postId, Integer value);

    @Query(value = "select count(*) as likesCount,(select count(*) from post_voters where value = -1) as dislikesCount," +
            " (select count(*) from posts) as postsCount," +
            " (select sum(posts.view_count) from posts) as viewsCount," +
            " (select min(posts.time) from posts) as firstPublication" +
            " from post_voters join posts on post_voters.post_id = posts.id where value = 1;", nativeQuery = true)
    Statistics getStatistics();

    @Query(value = "select count(*) as postsCount," +
            "(select count(*) where pv.value = 1) as likesCount," +
            "(select count(*) where pv.value = -1) as dislikesCount, " +
            "sum(p.view_count) as viewsCount, " +
            "min(p.time) as firstPublication " +
            "from posts p left join post_voters pv on pv.post_id = p.id where p.user_id = ?1",nativeQuery = true)
    Statistics getMyStatistics(Integer id);

    @Query(value = "select e from PostVotes e where e.postId = ?1 and e.userId = ?2")
    PostVotes getPostVotesByUserIdAndPostId(Integer postId, Integer userId);
}
