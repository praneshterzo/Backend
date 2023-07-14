package com.example.Portal.Repoistry;

import com.example.Portal.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepoistry extends JpaRepository<Employee, Integer> {

    public Employee findByEmpEmail(String email);

    Employee findByEmpId(int id);

    public void deleteByEmpId(int id);
}
