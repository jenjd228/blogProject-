package com.blog.spring.config;

import com.blog.spring.DTO.*;
import com.blog.spring.model.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                map(source.getCommentCount(), destination.getCommentList());
                map().setDislikeCount(source.getDislikeVotes());
                map().setLikeCount(source.getLikeVotes());
                map().setTags(source.getTags());
            }
        };
        PropertyMap<PostComments, CommentDTO> propertyMapToCommentDTO = new PropertyMap<>() {
            protected void configure() {
                map(source.getUser(), destination.getUser());
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
        System.out.println("dasdasd");
        PropertyMap<TagNameAndWeight, TagForTagsDTO> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {

            }
        };

        modelMapper.addMappings(propertyMap);
        return modelMapper;
        }

    }
