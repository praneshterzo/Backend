package com.example.Portal.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name="Login")
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;
    @ManyToOne()
    private Employee empl;
    @NonNull
    private String userEmail;
    @NonNull
    private String userPassword;
    private int Active;
    @ManyToOne
    private Role role1;

}
