package com.hospital.backend.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.backend.Models.Department;
import com.hospital.backend.Models.RoleStaff;
import com.hospital.backend.Models.RoleUser;
import com.hospital.backend.Models.Shift;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;


import java.util.List;
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;


    private String email;

    private String firstName;

    private String lastName;



    private String roleStaff;


    private String roleUser;
    private Integer vacationDays;

    private Long departmentId;
}
