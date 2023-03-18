package com.tdesh.songservice.repository;

import com.tdesh.songservice.entity.SongUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongUploadRepo extends JpaRepository<SongUpload,Long> {
}
