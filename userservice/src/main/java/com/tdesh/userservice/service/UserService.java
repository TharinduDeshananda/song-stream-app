package com.tdesh.userservice.service;

import com.tdesh.userservice.dto.request.UserRequestDTO;
import com.tdesh.userservice.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponseDTO getUser(long userId);
    void saveUser(UserRequestDTO userRequestDTO);
    Page<UserResponseDTO> getAllUsers(long userId, String userName,String email,int page,int size);

    UserResponseDTO getUserByEmail(String email);
}
