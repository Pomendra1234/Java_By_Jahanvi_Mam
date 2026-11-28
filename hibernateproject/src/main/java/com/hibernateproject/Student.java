package com.hibernateproject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name="students")
public class Student {

    public Student(int par, String rahul, double par1) {
    }
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    @Column (
        name="student_name",                //column name
        length=50,                         //varchar(100)
        nullable = false                    //non null
    )
    private String name;

    @Column (
        name="student_fees",                //column name
                                 //varchar(100)
        nullable = false                    //non null
    )
    private Double fees;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getFees() {
        return fees;
    }
    public void setFees(Double fees) {
        this.fees = fees;
    }
    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", fees=" + fees + "]";
    }
    
}
