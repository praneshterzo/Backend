package com.example.Portal.Repoistry;

import com.example.Portal.Models.Employee;
import com.example.Portal.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepoistry extends JpaRepository<Role,Integer> {
    Role findByRoleId(int id);

    Role findByRoleName(String name);
}
