package com.zoomers.GameSetMatch.entity;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name="User")
@DiscriminatorValue("E")
@Getter
@Setter
@ToString

public class Employee extends User{
    private @GeneratedValue int employeeId;
}
