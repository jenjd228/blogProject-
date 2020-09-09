package com.blog.spring.repository;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostsRepository extends CrudRepository<Posts,Integer> {
    List<Posts> findBy(Pageable pageable);
    Optional<Posts> findById(Integer id);


    //List<Posts> findAllOrderByCreatedOnAsc();

    //Posts findByText(String text);
}
