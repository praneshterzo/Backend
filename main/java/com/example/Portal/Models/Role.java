package com.example.Portal.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Role")
public class Role {
    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;
    private String roleName;
    @OneToMany(mappedBy = "role")
    private List<Employee> empl;
    @OneToMany(mappedBy = "role1")
    private List<Login>  roleid;
}
