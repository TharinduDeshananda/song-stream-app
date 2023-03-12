package com.tdesh.userservice.repository;

import com.tdesh.userservice.entity.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser,Long> {

    @Query(value = "SELECT cu FROM CustomUser cu WHERE (?1=0L OR ?1=cu.userId) AND (?2 IS NULL OR ?2=cu.userName) AND (?3 IS NULL OR ?3=cu.email)")
    Page<CustomUser> getUsersFiltered(long userId, String userName, String email, Pageable pageable);

    @Query(value = "SELECT cu FROM CustomUser cu WHERE ?1=cu.email")
    Optional<CustomUser> findUserByEmail(String email);
}
