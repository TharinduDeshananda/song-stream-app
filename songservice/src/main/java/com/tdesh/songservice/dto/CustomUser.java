package com.tdesh.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomUser {
    private long userId;
    private String userName;
    private String email;
    private String password;
    private List<String> authorities;
}
