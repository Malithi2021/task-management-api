package com.malithi.taskmanagement.service;

import com.malithi.taskmanagement.dto.UserDto;
import com.malithi.taskmanagement.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}