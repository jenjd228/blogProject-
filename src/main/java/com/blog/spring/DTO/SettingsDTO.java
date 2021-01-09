package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDTO {

    private boolean MULTIUSER_MODE;

    private boolean POST_PREMODERATION;

    private boolean STATISTICS_IS_PUBLIC;

}
