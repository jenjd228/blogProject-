package com.blog.spring.config;

import com.blog.spring.DTO.CommentDTO;
import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.DTO.TagForTagsDTO;
import com.blog.spring.DTO.UsersDTO;
import com.blog.spring.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ModelMapper modelMapperToPostsDTO() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PropertyMap<Posts, PostsDTO> propertyMap = new PropertyMap<>() {
            protected void configure() {
                map().setAnnounce(source.getText());
                map().setTimestamp(source.getTime());
                map().setDislikeCount(source.getDislikeVotes());
                map().setLikeCount(source.getLikeVotes());
                map().setCommentCount(source.getCommentCount());
            }
        };
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperForByIdPost() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PropertyMap<Posts, PostForGetByIdPost> propertyMap = new PropertyMap<>() {
            protected void configure() {
                map().setTimestamp(source.getTime());
                map().setActive(source.getIsActive());
                map(source.getUser(), destination.getUser());
                map(source.getCommentCount(),destination.getCommentList());
                map().setDislikeCount(source.getDislikeVotes());
                map().setLikeCount(source.getLikeVotes());
                map().setTags(source.getTags());
            }
        };
        PropertyMap<PostComments, CommentDTO> propertyMapToCommentDTO = new PropertyMap<>() {
            protected void configure() {
                map(source.getUser(),destination.getUser());
                map().setTimestamp(source.getTime());
            }
        };
        modelMapper.addMappings(propertyMap);
        modelMapper.addMappings(propertyMapToCommentDTO);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperToUserDTO() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Users, UsersDTO> propertyMap = new PropertyMap<>() {
            protected void configure() {
                map().setId(source.getId());
            }
        };
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperToTagForTagsDTO() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Tags, TagForTagsDTO> propertyMap = new PropertyMap<>() {
            protected void configure() {

                map().setWeight(source.getPosts());
            }
        };
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }


}
