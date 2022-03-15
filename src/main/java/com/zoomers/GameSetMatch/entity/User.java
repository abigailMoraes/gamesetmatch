package com.zoomers.GameSetMatch.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "User")

public class User {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int userID;
    private String email;
    private String name;
    private String firebase_id;
    // 0 is employee, 1 is admin, 2 is root admin
    private int is_admin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
