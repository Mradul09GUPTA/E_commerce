package com.example.userservices.repos;

import com.example.userservices.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session,Long> {

    Optional<Session> findByToken(String token);
}
