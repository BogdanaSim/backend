package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.sk.PrettyTable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Schedules")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Day> days;

    public String toString(List<User> users) {
        List<String> strings = new java.util.ArrayList<>(days.stream()
                .map(object -> Objects.toString(object, null))
                .toList());
        strings.add(0, "Firstname Lastname");
        String[] array = strings.toArray(new String[0]);
        PrettyTable table = new PrettyTable(array);
        for (User user : users) {
            List<Shift> shifts = new ArrayList<>();
            for (Day day : days) {
                if (day.getShifts().stream().anyMatch(o -> o.getUser().getId().equals(user.getId()))) {
                    shifts.add(day.getShifts().stream().filter(o -> o.getUser().getId().equals(user.getId())).findFirst().get());
                } else {
                    if (day.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || day.getDate().getDayOfWeek() == DayOfWeek.SUNDAY)
                        shifts.add(new Shift(1L, " ", user, day));
                    else
                        shifts.add(new Shift(1L, "L", user, day));
                }

            }
            strings = new java.util.ArrayList<>(shifts.stream()
                    .map(object -> Objects.toString(object, null))
                    .toList());
            strings.add(0, user.getFirstName() + " " + user.getLastName());
            array = strings.toArray(new String[0]);
            table.addRow(array);
        }
        return "Schedule{"+department.getName()+" " + date.getMonth() + " " + date.getYear() +
                '}' + "\n" + table;
    }
}
