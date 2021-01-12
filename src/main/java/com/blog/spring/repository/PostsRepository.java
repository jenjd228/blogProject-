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

    @Query("SELECT e FROM Posts e where e.isActive = ?1 AND e.userId = ?2 AND e.moderationStatus in ?3")
    List<Posts> findPostsByUserIdAndStatus(Pageable pageable,Integer isActive,Integer userId,Iterable<ModerationStatus> status);

    @Query("SELECT COUNT(*) FROM Posts e where e.isActive = ?1 AND e.userId = ?2 AND e.moderationStatus in ?3")
    long findCountPostsByUserIdAndStatus(Integer isActive,Integer userId,Iterable<ModerationStatus> status);

    @Query("select e from Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1")
    List<Posts> findBy(Pageable pageable,Long time);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 ORDER BY SIZE(e.likeVotes) DESC")
    List<Posts> findByLikeVotes(Pageable pageable, Long time);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 ORDER BY SIZE(e.commentCount) DESC")
    List<Posts> findByCommentCount(Pageable pageable, Long time);

    @Query("SELECT COUNT(e) FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1")
    long getPostCountByStandardConditions(Long time);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 AND (e.title like %?2% OR e.text like %?2%)")
    List<Posts> findByQuery(Pageable pageable, Long time,String search);

    @Query("SELECT COUNT(*) FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 AND (e.title like %?2% OR e.text like %?2%)")
    long getCountFindByQuery(Long time,String search);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 AND e.time between ?2 and ?3")
    List<Posts> findByDate(Pageable pageable, Long time, Long date, Long date2);

    @Query("SELECT COUNT(*) FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 AND e.time between ?2 and ?3")
    long getCountFindByDate(Long time, Long date, Long date2);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 AND e.id in ?2")
    List<Posts> findByIds(Pageable pageable, Long time, Iterable<Integer> ids);

    @Query("SELECT COUNT(*) FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1 AND e.id in ?2")
    long getCountFindByIds(Long time, Iterable<Integer> ids);

    Posts findPostsById(Integer id);

    @Query("SELECT COUNT(e) FROM Posts e where e.isActive = 1 AND e.moderationStatus = 'ACCEPTED' AND e.time <= ?1")
    long activePostCount(Long time);

    @Query("SELECT Count(e) FROM Posts e")
    long getPostCount();

    @Query("SELECT distinct DATE_FORMAT(FROM_UNIXTIME(time* 0.001), '%Y') from Posts")
    List<String> getCalendar();

    @Query(value ="SELECT time, COUNT(time) as count from ( SELECT DATE_FORMAT(FROM_UNIXTIME(time* 0.001), '%Y-%m-%d') as time from Posts where time >= ?1 and time < ?2 ) as t group by time", nativeQuery = true)
    List<List<String>> getCalendarByYear(Long fromDate, Long toDate);

    @Query("select e.id from Posts e where e.userId = ?1")
    List<Integer> getPostIdsByUserId(Integer userId);

    @Query("SELECT e FROM Posts e where e.isActive = 1 AND e.moderationStatus = ?1 and (e.moderatorId = ?2 or e.moderatorId is null)")
    List<Posts> findPostsByStatus(Pageable pageable, ModerationStatus status, Integer id);

    @Query("SELECT COUNT(*) FROM Posts e where e.isActive = 1 AND e.moderationStatus = ?1 and (e.moderatorId = ?2 or e.moderatorId is null)")
    long findPostsCountByStatus(ModerationStatus status, Integer id);

}
