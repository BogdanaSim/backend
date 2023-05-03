package com.hospital.backend.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.backend.Models.Day;
import com.hospital.backend.Models.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
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
