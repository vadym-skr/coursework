package com.example.demo.services;

import com.example.demo.entity.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// анотує класи на рівні сервісу
@Service
public class RoleService {
    // автоматичне підключенням компонента Spring.
    @Autowired
    private RoleRepository RoleRepository;

    public Role getRoleByName(String name) {
        return RoleRepository.getRoleByName(name);
    }
}
