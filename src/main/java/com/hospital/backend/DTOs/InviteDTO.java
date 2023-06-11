package com.hospital.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class InviteDTO {
    private Long id;

    private String roleStaff;

    private String email;

    private String roleUser;
    private Long departmentId;

}
