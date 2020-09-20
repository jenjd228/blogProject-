package com.blog.spring.config;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.Posts;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Posts, PostsDTO> propertyMap = new PropertyMap<Posts, PostsDTO> (){
            protected void configure() {
                map().setAnnounce(source.getText());
                map().setTimestamp(source.getTime());
                map().setDislikeCount(source.getDislikeVotes());
                map().setLikeCount(source.getLikeVotes());
                map().setCommentCount(source.getCommentCount());
            }
        };
        //modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(propertyMap);

        return modelMapper;
    }
}
