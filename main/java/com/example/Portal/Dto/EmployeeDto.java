package com.example.Portal.Dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class EmployeeDto {

    private int empId;
    private String empEmail;

    private String empPassword;

    private String empFirstname;

    private String empLastname;

    private String empAddress;

    private String empDesignation;

    private String empDepartment;

    private String empRole;

    private String empManager;

    private String empEmploymentType;

    private Date empJoiningDate;

    private long empSalary;

    private long empMobileNumber;

    private Date empDateofBirth;

    private int earnedLeave;

    private int sickLeave;

    private int paternityLeave;
}
