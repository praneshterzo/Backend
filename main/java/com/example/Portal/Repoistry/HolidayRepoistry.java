package com.example.Portal.Repoistry;

import com.example.Portal.Models.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepoistry extends JpaRepository<Holiday,Integer> {

}
