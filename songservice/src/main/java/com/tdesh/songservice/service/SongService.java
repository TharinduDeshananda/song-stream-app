package com.tdesh.songservice.service;

import com.tdesh.songservice.dto.SongSaveRequestDTO;
import com.tdesh.songservice.dto.UserUploadDetails;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface SongService {

    void addUserSong(SongSaveRequestDTO dto ) throws IOException;
    List<UserUploadDetails> getUserUploadDetails(long userId, Pageable pageable);
}
