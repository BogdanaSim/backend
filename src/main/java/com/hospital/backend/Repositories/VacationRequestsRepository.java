package com.hospital.backend.Repositories;

import com.hospital.backend.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRequestsRepository extends JpaRepository<VacationRequest,Long> {
    Optional<VacationRequest> findById(Long id);

    @Query("SELECT e FROM VacationRequest e WHERE (e.startDate >= ?1 AND e.startDate <= ?2) OR (e.endDate >= ?1 AND e.endDate <= ?2) AND e.status = 'APPROVED'")
    List<VacationRequest> findRequestsWithinDateRange(LocalDate start, LocalDate end);

    List<VacationRequest> findByStatusAndUserDepartment(StatusRequest status, Department user_department);

    @Query("SELECT e FROM VacationRequest e WHERE ((e.startDate <= :endDate AND e.endDate >= :startDate) OR (:startDate <= e.endDate AND :endDate >= e.startDate)  OR (e.startDate >= :startDate AND e.endDate <= :endDate) OR ( :startDate >=e.startDate AND :endDate <= e.endDate)) AND e.status = :statusRequest AND e.user.roleStaff = :role AND e.user.department.id = :departmentId")
    List<VacationRequest> findIntersectingRequests(LocalDate startDate, LocalDate endDate, RoleStaff role, Long departmentId, StatusRequest statusRequest);




}
