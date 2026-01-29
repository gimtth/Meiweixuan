package com.meiweixuan.dao;

import com.meiweixuan.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AdminDao extends JpaRepository<Admin, Integer> {
    
    Optional<Admin> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    @Modifying
    @Query("UPDATE Admin a SET a.lastLoginTime = ?2 WHERE a.id = ?1")
    void updateLastLoginTime(Integer adminId, Date lastLoginTime);
} 