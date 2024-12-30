package com.onion.backend.repository;

import com.onion.backend.entity.User;
import com.onion.backend.repository.user.UserRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);
}
