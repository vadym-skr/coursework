package com.example.demo.services;

import com.example.demo.entity.MyUserDetails;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
// анотує класи на рівні сервісу
@Service
// userDetailsService - використовується для отримання даних, пов'язаних з користувачем. У нього є один метод з ім'ям loadUserByUsername(), який можна перевизначити, щоб налаштувати процес пошуку користувача.
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
        return new MyUserDetails(user);
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal). getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
    public User getCurrentUser() {
        return findUserByUsername(getCurrentUsername());
    }
    public User findUserById(Integer id) {
        return userRepository.findUserById(id);
    }
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }
    public List<User> findUsersByAllFields(String username, String email, Boolean enabledOne, Boolean enabledTwo) {
        return userRepository.findUsersByAllFields(username, email, enabledOne, enabledTwo);
    }

    public Page<User> getForAllUsers(int pageNumber, int pageSize, String sortField, String username, String email, Boolean enabled, Role role) {
        username = "%" + username + "%";
        email = "%" + email + "%";
        boolean enabledOne = true;
        boolean enabledTwo = false;
        if(enabled != null) {
            if(enabled) {
                enabledOne = true;
                enabledTwo = true;
            }
            else {
                enabledOne = false;
                enabledTwo = false;
            }
        }

        Sort sort = Sort.by("id").ascending();
        if(sortField != null && !sortField.isEmpty()) {
            if(sortField.equals("id up"))
                sort = Sort.by("id").ascending();
            if(sortField.equals("id down"))
                sort = Sort.by("id").descending();
            if(sortField.equals("username up"))
                sort = Sort.by("username").ascending();
            if(sortField.equals("username down"))
                sort = Sort.by("username").descending();
            if(sortField.equals("email up"))
                sort = Sort.by("email").ascending();
            if(sortField.equals("email down"))
                sort = Sort.by("email").descending();
            if(sortField.equals("enabled up"))
                sort = Sort.by("enabled").ascending();
            if(sortField.equals("enabled down"))
                sort = Sort.by("enabled").descending();
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findForPageAllUsers(username, email, enabledOne, enabledTwo, role, pageable);
        return page;
    }

    public void create(User user, Role role) {
        user.setPassword(user.encodePassword(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(Collections.singleton(role));
        //user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
    }
    public void save(User user) {
        userRepository.save(user);
    }
    public void saveAndUpdateCurrentUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        if(!user.getUsername().equals(userDetails.getUser().getUsername()))
            userDetails.getUser().setUsername(user.getUsername());
        if(!user.getPassword().equals(userDetails.getUser().getPassword()))
            userDetails.getUser().setPassword(user.getPassword());
        userRepository.save(user);
    }
}
