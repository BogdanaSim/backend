package com.hospital.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Entity
@Table(name = "Users_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Details")
    @Length(min = 5, max = 1000)
    private String details;

    @Column(name = "Phone_number")
    @Length(min = 4, max = 1000)
    private String phoneNumber;

    @Lob
    @Column(name = "Profile_picture")
    @ToString.Exclude
    private byte[] profilePicture;

    @OneToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "Id")
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfile that = (UserProfile) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
