package com.tdesh.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;
import org.hibernate.type.StandardBasicTypes;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser {

    @Id
    @GeneratedValue
    private long userId;
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority",joinColumns = @JoinColumn(name = "user_id"))
    private List<String> authorities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "subscriber_id")
            ,uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","subscriber_id"}))
    private List<CustomUser> subscribers;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "publisher_id")
            ,uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "publisher_id"}))
    private List<CustomUser> publishers;

    @PrePersist
    @PreUpdate
    public void checkForConstraints(){
        if(subscribers!=null && subscribers.stream().anyMatch(s->s.userId == userId))throw new RuntimeException("Operation failed, cannot subscribe to self");
        if(publishers!=null && publishers.stream().anyMatch(p->p.userId == userId))throw new RuntimeException("Operation failed, cannot add self as a publisher");
    }

}
