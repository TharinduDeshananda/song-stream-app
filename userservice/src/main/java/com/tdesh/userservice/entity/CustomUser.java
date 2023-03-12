package com.tdesh.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;
import org.hibernate.type.StandardBasicTypes;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser {

    @Id
    @GeneratedValue
    private long userId;
    private String userName;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority",joinColumns = @JoinColumn(name = "user_id"))
    private List<String> authorities;

}
