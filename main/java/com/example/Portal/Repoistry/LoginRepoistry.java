package com.example.Portal.Repoistry;

import com.example.Portal.Models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepoistry extends JpaRepository<Login,Integer> {
}
