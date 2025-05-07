package com.example.backend.services;

import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user)
    {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id)
    {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String id, User user)
    {
        if (userRepository.existsById(id)) {
            User existingUser = userRepository.findById(id).orElse(null);

            if (existingUser != null) {
                if (user.getUsername() != null) {
                    existingUser.setUsername(user.getUsername());
                }
                if (user.getEmail() != null) {
                    existingUser.setEmail(user.getEmail());
                }
                if (user.getPassword() != null) {
                    existingUser.setPassword(user.getPassword());
                }

                return userRepository.save(existingUser);
            }
        }
        return null;
    }

    public void deleteUser(String id)
    {
        userRepository.deleteById(id);
    }
}
