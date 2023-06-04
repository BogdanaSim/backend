package com.hospital.backend.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
