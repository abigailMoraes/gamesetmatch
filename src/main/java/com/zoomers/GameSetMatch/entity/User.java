package com.zoomers.GameSetMatch.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)


public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userID")
    private Long userId;
    @Column(name="firebase_id")
    private String companyId;
    private String name;
    private String email;
    @Column(name="is_admin")
    private int isAdmin ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(user.getUserId(), user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
