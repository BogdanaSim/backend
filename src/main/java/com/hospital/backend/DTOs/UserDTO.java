package com.hospital.backend.DTOs;


import lombok.*;

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
