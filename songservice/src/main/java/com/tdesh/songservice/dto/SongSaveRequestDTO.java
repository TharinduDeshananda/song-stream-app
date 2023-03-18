package com.tdesh.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongSaveRequestDTO {
    private MultipartFile file;
    private String uploadTitle;
}
