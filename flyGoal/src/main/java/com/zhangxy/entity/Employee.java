package com.zhangxy.entity;

import javax.persistence.Entity;

import com.zhangxy.entity.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class Employee  extends BaseEntity{

    private String firstName, lastName, description;

    protected Employee() {}

    public Employee(final String firstName, final String lastName, final String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }
}

