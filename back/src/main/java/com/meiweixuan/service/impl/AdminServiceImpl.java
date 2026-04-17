package com.meiweixuan.service.impl;

import com.meiweixuan.dao.AdminDao;
import com.meiweixuan.dto.LoginRequest;
import com.meiweixuan.dto.LoginResponse;
import com.meiweixuan.entity.Admin;
import com.meiweixuan.service.AdminService;
import com.meiweixuan.util.JwtTokenUtil;
import com.meiweixuan.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminDao adminDao;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordUtil passwordUtil;
    
    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<Admin> adminOptional = adminDao.findByUsername(loginRequest.getUsername());
        
        if (!adminOptional.isPresent()) {
            throw new RuntimeException("管理员不存在");
        }
        
        Admin admin = adminOptional.get();
        
        if (!passwordUtil.matches(loginRequest.getPassword(), admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (passwordUtil.needsUpgrade(admin.getPassword())) {
            admin.setPassword(passwordUtil.encode(loginRequest.getPassword()));
        }
        
        if (admin.getStatus() != 1) {
            throw new RuntimeException("管理员已被禁用");
        }
        
        // 更新最后登录时间
        adminDao.updateLastLoginTime(admin.getId(), new Date());
        
        // 生成token
        String token = jwtTokenUtil.generateToken(admin.getUsername(), admin.getId(), "admin");
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(admin.getId());
        loginResponse.setUsername(admin.getUsername());
        loginResponse.setName(admin.getName());
        loginResponse.setPhone(admin.getPhone());
        loginResponse.setRole("ROLE_ADMIN");
        loginResponse.setToken(token);
        loginResponse.setUserType("admin");
        
        return loginResponse;
    }
    
    @Override
    public Admin getAdminById(Integer id) {
        return adminDao.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
    }
    
    @Override
    @Transactional
    public Admin updateAdmin(Admin admin) {
        Admin existingAdmin = getAdminById(admin.getId());
        
        existingAdmin.setName(admin.getName());
        existingAdmin.setPhone(admin.getPhone());
        
        return adminDao.save(existingAdmin);
    }
} 
