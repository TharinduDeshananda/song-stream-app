package com.tdesh.songservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongUpload {
    @Id
    @GeneratedValue
    private long songUploadId;
    private String uploadTitle;
    private long userId;
    private String uploadUrl;
    private String userEmail;
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date uploadDateTime;
    @Temporal(value = TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateDateTime;
}
