package com.tdesh.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private long userId;
    private String userName;
    private String email;
    private String password;
    private List<String> authorities;
}
