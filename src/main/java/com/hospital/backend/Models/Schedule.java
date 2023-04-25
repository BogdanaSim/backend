package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Schedule")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM")
    @Column(name = "Date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Day> days;
}
