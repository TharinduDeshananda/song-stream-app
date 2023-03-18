package com.tdesh.userservice.dto.request;

import com.tdesh.userservice.dto.response.UserResponseDTO;
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
