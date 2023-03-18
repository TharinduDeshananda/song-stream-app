package com.tdesh.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUploadDetails {
    private long songUploadId;
    private String uploadTitle;
    private long userId;
    private String uploadUrl;
    private String userEmail;
    private Date uploadDateTime;
    private Date updateDateTime;
}
