package com.hospital.backend.Repositories;

import com.hospital.backend.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    void deleteById(Long id);

    void deleteByEmail(String email);
}
