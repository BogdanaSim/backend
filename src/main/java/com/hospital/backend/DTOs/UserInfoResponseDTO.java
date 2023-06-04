package com.hospital.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDTO {

    private Long id;
    private String email;
    private String roles;
}
