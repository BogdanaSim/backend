package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Shift> shifts;

    @OneToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    @JsonIgnore
    private Department department;

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
}
