package com.tdesh.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSubscriberResponseDTO {
    private long userId;
    private String email;
    private List<UserResponseDTO> subscribers;
}
