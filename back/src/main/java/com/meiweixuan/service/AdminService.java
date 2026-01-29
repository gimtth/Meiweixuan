package com.meiweixuan.service;

import com.meiweixuan.dto.LoginRequest;
import com.meiweixuan.dto.LoginResponse;
import com.meiweixuan.entity.Admin;

public interface AdminService {
    
    LoginResponse login(LoginRequest loginRequest);
    
    Admin getAdminById(Integer id);
    
    Admin updateAdmin(Admin admin);
} 