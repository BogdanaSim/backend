package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentsRepository extends JpaRepository<Department,Long> {
    @Override
    Optional<Department> findById(Long aLong);
}
