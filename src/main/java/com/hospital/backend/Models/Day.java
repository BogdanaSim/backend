package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Days")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Shift> shifts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Schedule_id")
    @ToString.Exclude
    @JsonIgnore
    private Schedule schedule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Day day = (Day) o;
        return getId() != null && Objects.equals(getId(), day.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
