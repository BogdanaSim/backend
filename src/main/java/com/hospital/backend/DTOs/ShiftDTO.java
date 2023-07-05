package com.hospital.backend.DTOs;


import lombok.*;
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDTO {

    private Long id;

    private String type;


    private Long userId;

    private Long dayId;
}
