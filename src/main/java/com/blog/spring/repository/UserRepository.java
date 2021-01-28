package com.blog.spring.repository;

import com.blog.spring.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {
    Users findByEmail(String email);

    Users findUsersByCode(String code);

    Users findUsersById(Integer id);
}
