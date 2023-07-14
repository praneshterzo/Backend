package com.example.Portal.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="`Leave`")
public class Leave {
    @ManyToOne
    @JoinColumn(name="empId")
    private Employee emplo;
    @Id
    private int leaveId;
    public static int Medicalleave;
    public static int PaidLeave;
    public static int Vacationdays;


}
