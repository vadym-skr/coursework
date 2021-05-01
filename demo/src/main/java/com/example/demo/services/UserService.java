package com.example.demo.services;

import com.example.demo.entity.MyUserDetails;
import com.example.demo.entity.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
// анотує класи на рівні сервісу
@Service
// userDetailsService - використовується для отримання даних, пов'язаних з користувачем. У нього є один метод з ім'ям loadUserByUsername(), який можна перевизначити, щоб налаштувати процес пошуку користувача.
public class UserService implements UserDetailsService {
    // автоматичне підключенням компонента Spring.
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
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return findUserByUsername(userDetails.getUsername());
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
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }
    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
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
