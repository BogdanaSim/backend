package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "invites")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, name = "email")
    @Length(min = 5, max = 50)
    @Email
    private String email;

    @Column(name = "role_staff")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleStaff roleStaff;

    @Column(name = "role_user")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleUser roleUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    @JsonIgnore
    private Department department;

}
