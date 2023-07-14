package com.example.Portal.Models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "EmpDetails")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    @Column(name="emp_email")
    private String empEmail;
    private String empFirstname;
    private String empLastname;
    @Column(name="emp_password")
    private String empPassword;
    @Column(name="emp_address")
    private String empAddress;
    @Column(name="emp_designation")
    private String empDesignation;
    @Column(name="emp_department")
    private String empDepartment;
    @Column(name="emp_role")
    private String empRole;
    @Column(name="emp_manager")
    private String empManager;
    @Column(name="emp_employement_type")
    private String empEmploymentType;
    @Column(name="emp_joining_date")
    private Date empJoiningDate;
    @Column(name="emp_salary")
    private long empSalary;
    @Column(name="emp_mobile_number")
    private Long empMobileNumber;
    @Column(name="emp_dateof_birth")
    private Date empDatefBirth;

    private boolean isActive;

    private int earnedLeave;
    private int sickLeave;
    private int paternityLeave;

    @OneToMany(mappedBy = "emplo")
    private List<Leave> leaves;

    @OneToMany(mappedBy = "employee")
    private List<ApplyLeave> applyLeaves;

    @OneToMany(mappedBy = "empl")
    private List<Login> logins;

    @ManyToOne
    private Role role;
}
