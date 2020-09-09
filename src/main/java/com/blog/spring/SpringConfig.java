package com.blog.spring;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.DTO.UsersDTO;
import com.blog.spring.model.Posts;
import com.blog.spring.repository.PostCommentsRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.print.attribute.standard.Destination;
import javax.xml.transform.Source;
import java.time.ZoneOffset;

@Configuration
public class SpringConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Posts, PostsDTO> propertyMap = new PropertyMap<Posts, PostsDTO> (){
            protected void configure() {
                map().setAnnounce(source.getText());
                map().setTimestamp(source.getTime());
            }
        };
        //modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(propertyMap);

        return modelMapper;
    }
}
