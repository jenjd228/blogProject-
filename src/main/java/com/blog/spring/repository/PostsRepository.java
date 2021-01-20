package com.blog.spring.repository;

import com.blog.spring.model.ModerationStatus;
import com.blog.spring.model.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends CrudRepository<Posts, Long> {

    @Query("select e from Posts e where e.isActive = ?1 and e.userId = ?2 and e.moderationStatus in ?3")
    List<Posts> findPostsByUserIdAndStatus(Pageable pageable, Integer isActive, Integer userId, Iterable<ModerationStatus> status);

    @Query("select count(*) from Posts e where e.isActive = ?1 and e.userId = ?2 and e.moderationStatus in ?3")
    long findCountPostsByUserIdAndStatus(Integer isActive, Integer userId, Iterable<ModerationStatus> status);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1")
    List<Posts> findBy(Pageable pageable, Long time);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 order by size(e.likeVotes) desc")
    List<Posts> findByLikeVotes(Pageable pageable, Long time);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 order by size(e.commentCount) desc")
    List<Posts> findByCommentCount(Pageable pageable, Long time);

    @Query("select count(e) from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1")
    long getPostCountByStandardConditions(Long time);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 and (e.title like %?2% or e.text like %?2%)")
    List<Posts> findByQuery(Pageable pageable, Long time, String search);

    @Query("select count(*) from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 and (e.title like %?2% or e.text like %?2%)")
    long getCountFindByQuery(Long time, String search);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 and e.time between ?2 and ?3")
    List<Posts> findByDate(Pageable pageable, Long time, Long date, Long date2);

    @Query("select count(*) from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 and e.time between ?2 and ?3")
    long getCountFindByDate(Long time, Long date, Long date2);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 and e.id in ?2")
    List<Posts> findByIds(Pageable pageable, Long time, Iterable<Integer> ids);

    @Query("select count(*) from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1 and e.id in ?2")
    long getCountFindByIds(Long time, Iterable<Integer> ids);

    Posts findPostsById(Integer id);

    @Query("select count(e) from Posts e where e.isActive = 1 and e.moderationStatus = 'ACCEPTED' and e.time <= ?1")
    long activePostCount(Long time);

    @Query("select count(e) from Posts e where e.isActive = 1 and e.moderationStatus = 'NEW' and e.moderatorId is null")
    long getCountPostForModeration();

    @Query("select distinct date_format(from_unixtime(time), '%Y') from Posts")
    List<String> getCalendar();

    @Query(value = "select time, count(time) as count from ( select date_format(from_unixtime(time), '%Y-%m-%d') as time from Posts where time >= ?1 and time < ?2 and moderation_status = 'ACCEPTED' ) as t group by time", nativeQuery = true)
    List<List<String>> getCalendarByYear(Long fromDate, Long toDate);

    @Query("select e.id from Posts e where e.userId = ?1")
    List<Integer> getPostIdsByUserId(Integer userId);

    @Query("select e from Posts e where e.isActive = 1 and e.moderationStatus = ?1 and (e.moderatorId = ?2 or e.moderatorId is null)")
    List<Posts> findPostsByStatus(Pageable pageable, ModerationStatus status, Integer id);

    @Query("select count(*) from Posts e where e.isActive = 1 and e.moderationStatus = ?1 and (e.moderatorId = ?2 or e.moderatorId is null)")
    long findPostsCountByStatus(ModerationStatus status, Integer id);

}
