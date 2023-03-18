package com.tdesh.songservice.dto;

import com.tdesh.songservice.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDTO {
    private String email;
    private EmailType emailType;
    private Map<String,String> attributes;
}

