package com.hospital.backend.Repositories;

import com.hospital.backend.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedule,Long> {
    Boolean existsByDate(LocalDate date);
    Optional<Schedule> findByDate(LocalDate date);
    List<Schedule> findScheduleByDepartment(Department department);
    List<Schedule> findByDepartmentAndScheduleStatus(Department department, ScheduleStatus scheduleStatus);
    Optional<Schedule> findScheduleByDateAndDepartment(LocalDate date, Department department);
    Optional<Schedule> findSchedulesByDateAndDepartmentAndRoleStaff(LocalDate date, Department department, RoleStaff roleStaff);
    Optional<Schedule> findSchedulesByDateAndDepartmentAndRoleStaffAndScheduleStatus(LocalDate date, Department department, RoleStaff roleStaff, ScheduleStatus scheduleStatus);
    @Modifying
    @Query("update Schedule s set s.scheduleType = ?1 where s.id = ?2")
    int setTypeForSchedule(ScheduleType scheduleType, Long id);
}
