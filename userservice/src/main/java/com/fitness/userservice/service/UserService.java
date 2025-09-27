package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public UserResponse register(RegisterRequest request) {

        // TODO: Replace RuntimeException with a custom, more specific exception like EmailAlreadyExistsException.Internal error returned.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // TODO: This manual mapping from Request to Entity is repetitive and error-prone. Use a mapper like MapStruct.
        User user = new User();
        user.setEmail(request.getEmail());
        // FIXME: CRITICAL SECURITY ISSUE - Never save a plain text password.
        // Inject Spring's PasswordEncoder and hash the password before setting it.
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userRepository.save(user);

        // TODO: This manual mapping from Entity to Response is also repetitive. Use a mapper.
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());

        // FIXME: CRITICAL SECURITY ISSUE - Never return the user's password in the response, not even the hash.
        // The response object should not have a password field.
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;

    }

    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // TODO: This manual mapping from Entity to Response is also repetitive. Use a mapper.
        User savedUser = userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());

        // FIXME: CRITICAL SECURITY ISSUE - Never return the user's password in the response, not even the hash.
        // The response object should not have a password field.
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;

    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
