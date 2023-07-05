package com.hospital.backend.Repositories;

import com.hospital.backend.Models.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitesRepository extends JpaRepository<Invite,Long> {

    List<Invite> findAllByDepartment_Id(Long department_id);

    Optional<Invite> findFirstByEmail(String email);
}
