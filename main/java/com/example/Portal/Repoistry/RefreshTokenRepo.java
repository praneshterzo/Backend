package com.example.Portal.Repoistry;


import com.example.Portal.Models.RefreshToken;
import com.example.Portal.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    RefreshToken findByToken(String token);

    RefreshToken findByEmployee(Employee employee);
}