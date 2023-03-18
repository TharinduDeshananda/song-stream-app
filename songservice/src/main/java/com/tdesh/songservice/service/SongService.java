package com.tdesh.songservice.service;

import com.tdesh.songservice.dto.SongSaveRequestDTO;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface SongService {

    void addUserSong(SongSaveRequestDTO dto ) throws IOException;
}
