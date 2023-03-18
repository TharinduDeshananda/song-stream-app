package com.tdesh.userservice.service;

import com.tdesh.userservice.dto.request.UserPublisherResponseDTO;
import com.tdesh.userservice.dto.request.UserRequestDTO;
import com.tdesh.userservice.dto.request.UserSubscriberResponseDTO;
import com.tdesh.userservice.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponseDTO getUser(long userId);
    void saveUser(UserRequestDTO userRequestDTO);
    Page<UserResponseDTO> getAllUsers(long userId, String userName,String email,int page,int size);
    void addSubscriberToPublisher(long subscriberId,long publisherId);
    void removeSubscriberFromPublisher(long subscriberId,long publisherId);
    UserPublisherResponseDTO getUserPublishers(String email);
    UserSubscriberResponseDTO getUserSubscribers(String email);
    UserResponseDTO getUserByEmail(String email);
}
