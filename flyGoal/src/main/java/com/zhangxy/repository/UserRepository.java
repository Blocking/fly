package com.zhangxy.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.zhangxy.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{

    User findFirstByUsername(String username);
    
    User findOneByUsername(String username);
    
    List<User> findAll();
    
}