package com.meiweixuan.dao;

import com.meiweixuan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    @Modifying
    @Query("UPDATE User u SET u.lastLoginTime = ?2 WHERE u.id = ?1")
    void updateLastLoginTime(Integer userId, Date lastLoginTime);
} 