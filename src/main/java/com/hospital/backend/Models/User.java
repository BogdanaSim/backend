package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, name = "email")
    @Length(min = 5, max = 50)
    @Email
    private String email;

    @Column(name = "firstName")
    @Length(min = 5, max = 1000)
    private String firstName;

    @Column(name = "lastName")
    @Length(min = 5, max = 1000)
    private String lastName;


    @Column(name = "password")
    @Length(min = 5, max = 255)
    private String password;

    @Column(name = "role_staff")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleStaff roleStaff;

    @Column(name = "role_user")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleUser roleUser;

    @Column(name = "vacation_days")
    private Integer vacationDays;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Shift> shifts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<VacationRequest> vacationRequests;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName ;
    }
    public int getNoHours(LocalDate startDate, LocalDate endDate){
        List<Shift> shiftsUser = new ArrayList<>();
        shiftsUser.addAll(shifts.stream().filter(item-> item.getDay().getDate().isAfter(startDate) && item.getDay().getDate().isBefore(endDate)).toList());

        int noHours=0;
        List<String> options12H = List.of(ShiftTypes.MORNING.getValue(),ShiftTypes.NIGHT.getValue());
        List<String> options8H = List.of(ShiftTypes.SHORT.getValue());
        List<String> optionsFree = List.of(ShiftTypes.SHORT.getValue());


        for(Shift shift:shiftsUser){
            if(options12H.contains(shift.getType())){
                noHours+=12;
            } else if (options8H.contains(shift.getType())) {
                noHours+=8;
            }
        }
        return noHours;
    }

    public User(String email, String password,String roleStaff,String firstName,String lastName) {
        this.email = email;
        this.password = password;
        this.vacationDays=21;
        this.roleUser=RoleUser.USER;
        this.firstName=firstName;
        this.lastName=lastName;
        this.roleStaff=RoleStaff.valueOf(roleStaff);
        this.department=new Department();
        this.department.setId(1L);
    }

}
