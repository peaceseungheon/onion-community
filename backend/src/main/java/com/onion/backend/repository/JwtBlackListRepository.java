package com.onion.backend.repository;

import com.onion.backend.entity.JwtBlackList;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlackListRepository extends JpaRepository<JwtBlackList, Long> {
    void deleteByExpirationTimeBefore(Instant time);

    Optional<JwtBlackList> findFirstByEmailOrderByExpirationTimeDesc(String email);
}
