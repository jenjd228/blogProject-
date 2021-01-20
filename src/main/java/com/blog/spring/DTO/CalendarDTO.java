package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {
    private List<String> years;

    private HashMap<String,String> posts;
}
