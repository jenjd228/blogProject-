package com.blog.spring.config;

import com.blog.spring.DTO.*;
import com.blog.spring.model.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
public class SpringConfig {

    @Value("${domain}")
    private String domain;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapperToPostsDTO() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PropertyMap<Posts, PostsDTO> propertyMap = new PropertyMap<>() {
            protected void configure() {
                map().setAnnounceWithoutHtml(source.getText());
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
        PropertyMap<TagNameAndWeight, TagForTagsDTO> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {

            }
        };
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperToStatisticDTO() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Statistics, StatisticDTO> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {

            }
        };
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapperToUserLoginDTO() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Users, UserLoginDTO> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setModerationInModelMapper(source.getIsModerator());
                map().setSettingsInModelMapper(source.getIsModerator());
            }
        };
        modelMapper.addMappings(propertyMap);
        return modelMapper;
    }


}
