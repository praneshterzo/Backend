package com.example.Portal.Repoistry;

import com.example.Portal.Models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Leaverepoistry extends JpaRepository<Leave,Integer> {
    //Leave findByEmplo(int id);
}
