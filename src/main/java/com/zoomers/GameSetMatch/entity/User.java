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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "userID")
    private int id;
    @Column (name = "firebase_id")
    private String firebaseId;
    @Column (name = "email")
    private String email;
    @Column (name = "name")
    private String name;
    // 0 is employee, 1 is admin, 2 is root admin
    @Column (name = "is_admin")
    private int isAdmin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return String.format("{\"id\":%d,\"firebaseId\":\"%s\",\"email\":\"%s\",\"name\":\"%s\"," +
                "\"isAdmin\":%d}", this.id, this.firebaseId, this.email, this.name, this.isAdmin);
    }
}
