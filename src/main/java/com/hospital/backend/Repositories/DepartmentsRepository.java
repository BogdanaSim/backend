package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentsRepository extends JpaRepository<Department,Long> {
    @Override
    Optional<Department> findById(Long aLong);
}
