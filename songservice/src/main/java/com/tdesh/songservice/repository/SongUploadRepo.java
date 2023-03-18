package com.tdesh.songservice.repository;

import com.tdesh.songservice.entity.SongUpload;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SongUploadRepo extends JpaRepository<SongUpload,Long> {

    @Query(value = "SELECT su FROM SongUpload su WHERE su.userId=?1")
    List<SongUpload> getSongUploadsOfUser(long userID, Pageable pageable);
}
