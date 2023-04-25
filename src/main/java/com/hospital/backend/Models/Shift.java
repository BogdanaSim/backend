package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Shifts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Type")
    @Length(min = 1, max = 2)
    private String type;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Day_id")
    @ToString.Exclude
    @JsonIgnore
    private Day day;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Shift shift = (Shift) o;
        return getId() != null && Objects.equals(getId(), shift.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
