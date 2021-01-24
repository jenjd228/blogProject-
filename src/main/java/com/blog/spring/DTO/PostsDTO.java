package com.blog.spring.DTO;

import com.blog.spring.model.PostComments;
import com.blog.spring.model.PostVotes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsDTO  {

    private Integer id;

    private Long timestamp;

    private UsersDTO user;

    private String title;

    private String announce;

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer commentCount;

    private Integer viewCount;

    public void setAnnounceWithoutHtml(String announceWithHtml){
        this.announce = announceWithHtml.replaceAll("<.+?>", "").replaceAll("(\\s)+", "$1");
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLikeCount(List<PostVotes> list){ this.likeCount = list.size(); }

    public void setDislikeCount(List<PostVotes> list){ this.dislikeCount = list.size(); }

    public void setCommentCount(List<PostComments> list){ this.commentCount = list.size(); }
}
