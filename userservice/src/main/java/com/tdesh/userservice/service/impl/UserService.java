package com.tdesh.userservice.service.impl;

import com.tdesh.userservice.dto.request.UserPublisherResponseDTO;
import com.tdesh.userservice.dto.request.UserRequestDTO;
import com.tdesh.userservice.dto.request.UserSubscriberResponseDTO;
import com.tdesh.userservice.dto.response.UserResponseDTO;
import com.tdesh.userservice.entity.CustomUser;
import com.tdesh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class UserService implements com.tdesh.userservice.service.UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public UserResponseDTO getUser(long userId) {
        try {
            log.info("Method getUser: userId {}",userId);
            Optional<CustomUser> userOptional = userRepository.findById(userId);
            CustomUser user = userOptional.orElseThrow(() -> new RuntimeException("User Not found"));
            UserResponseDTO dto = modelMapper.map(user,UserResponseDTO.class);
            log.info("Method getUser: userId {} success",userId);
            return dto;
        } catch (Exception e) {
            log.info("Method getUser: userId {} failed",userId);
            throw e;
        }
    }

    @Override
    public void saveUser(UserRequestDTO userRequestDTO) {
        try {
            log.info("Method saveUser");
            CustomUser customUser = modelMapper.map(userRequestDTO,CustomUser.class);
            userRepository.save(customUser);
            log.info("Method saveUser success");
        } catch (Exception e) {
            log.info("Method saveUser failed");
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Page<UserResponseDTO> getAllUsers(long userId, String userName,String email,int page,int size) {
        try {
            log.info("Method getAllUsers userId {} , userName {} , email: {}",userId,userName,email);
            Page<UserResponseDTO> usersFiltered = userRepository.getUsersFiltered(userId, userName, email,size!=0? PageRequest.of(page,size):Pageable.unpaged()).map(cu->modelMapper.map(cu,UserResponseDTO.class));
            log.info("Method getAllUsers userId {} , userName {} , email: {} success",userId,userName,email);
            return usersFiltered;
        } catch (Exception e) {
            log.info("Method getAllUsers userId {} , userName {} , email: {} failed",userId,userName,email);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addSubscriberToPublisher(long subscriberId, long publisherId) {
        try {
            log.info("Method addSubscriberToPublisher subscriber: {} , publisher: {}",subscriberId,publisherId);
            CustomUser subscriber = userRepository.findById(subscriberId).orElseThrow(()->new RuntimeException("Not found subscriber"));
            CustomUser publisher = userRepository.findById(publisherId).orElseThrow(()->new RuntimeException("Not found publisher"));


            subscriber.getPublishers().add(publisher);
            publisher.getSubscribers().add(subscriber);

            userRepository.save(subscriber);
            userRepository.save(publisher);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void removeSubscriberFromPublisher(long subscriberId, long publisherId) {
        try {
            log.info("Method removeSubscriberFromPublisher subscriber: {} , publisher: {}",subscriberId,publisherId);
            CustomUser subscriber = userRepository.findById(subscriberId).orElseThrow(()->new RuntimeException("Not found subscriber"));
            CustomUser publisher = userRepository.findById(publisherId).orElseThrow(()->new RuntimeException("Not found publisher"));


            subscriber.getPublishers().remove(publisher);
            publisher.getSubscribers().remove(subscriber);

            userRepository.save(subscriber);
            userRepository.save(publisher);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public UserPublisherResponseDTO getUserPublishers(String email) {
        try {
            log.info("Method getUserPublishers {}",email);

            CustomUser customUser = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("OPeration failed, user not found"));

            return UserPublisherResponseDTO.builder()
                    .userId(customUser.getUserId())
                    .email(customUser.getEmail())
                    .publishers(customUser.getPublishers().stream().map(p->modelMapper.map(p,UserResponseDTO.class)).collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public UserSubscriberResponseDTO getUserSubscribers(String email) {
        try {
            log.info("Method getUserSubscribers {}",email);
            CustomUser customUser = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("OPeration failed, user not found"));

            return UserSubscriberResponseDTO.builder()
                    .userId(customUser.getUserId())
                    .email(customUser.getEmail())
                    .subscribers(customUser.getSubscribers().stream().map(p->modelMapper.map(p,UserResponseDTO.class)).collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        try {
            log.info("Method getUserByEmail : email {}",email);
            CustomUser user = userRepository.findUserByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
            UserResponseDTO map = modelMapper.map(user, UserResponseDTO.class);
            log.info("Method getUserByEmail : email {} success",email);
            return map;
        } catch (Exception e) {
            log.info("Method getUserByEmail : email {} failed",email);
            throw e;
        }
    }


}
