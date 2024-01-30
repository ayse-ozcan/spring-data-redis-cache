package com.ayseozcan.service;

import com.ayseozcan.repository.IUserRepository;
import com.ayseozcan.repository.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String saveUser(String username) {
        User user = new User();
        user.setUsername(username);
        userRepository.save(user);
        return "registration successful";
    }

    @Cacheable(value = "user", key = "#username")
    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user;
        }
        throw new RuntimeException("user does not exist");
    }

    @CacheEvict(value = "user" , allEntries = true)
    public String clearCache(){
        return "cache cleared";
    }
}
