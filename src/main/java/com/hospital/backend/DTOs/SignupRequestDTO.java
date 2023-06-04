package com.hospital.backend.DTOs;

import com.hospital.backend.Models.RoleStaff;
import com.hospital.backend.Models.RoleUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {


    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String roleUser = RoleUser.USER.toString();

    private String roleStaff = RoleStaff.NURSE.toString();

    @NotBlank
    @Size(min = 6, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 6, max = 40)
    private String lastName;


    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
