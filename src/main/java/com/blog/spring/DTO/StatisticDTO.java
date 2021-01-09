package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {

    private long likesCount;

    private long dislikesCount;

    private int postsCount;

    private long viewsCount;

    private long firstPublication;
}
