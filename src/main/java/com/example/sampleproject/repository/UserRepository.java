package com.example.sampleproject.repository;

import com.example.sampleproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    User findByUserName(String userName);
}
