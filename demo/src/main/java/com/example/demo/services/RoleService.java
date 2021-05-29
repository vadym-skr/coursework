package com.example.demo.services;

import com.example.demo.entity.user.Role;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// анотує класи на рівні сервісу
@Service
public class RoleService {
    // автоматичне підключенням компонента Spring.
    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public void deleteUsersRolesForUser(Integer id) {
        roleRepository.deleteUsersRolesForUser(id);
    }
}
