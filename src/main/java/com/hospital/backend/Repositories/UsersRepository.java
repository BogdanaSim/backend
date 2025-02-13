package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.RoleStaff;
import com.hospital.backend.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleStaff(RoleStaff roleStaff);

    List<User> findByDepartmentId(Long department_id);
    boolean existsByEmail(String email);
    @Transactional
    void deleteById(Long id);

    void deleteByEmail(String email);

    List<User> findByRoleStaffAndDepartment(RoleStaff roleStaff, Department department);
}
