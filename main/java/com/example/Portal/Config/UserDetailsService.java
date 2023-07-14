package com.example.Portal.Config;

import com.example.Portal.Dto.EmployeeDto;
import com.example.Portal.Dto.UserDetailsDto;
import com.example.Portal.Models.Employee;
import com.example.Portal.Repoistry.EmployeeRepoistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    EmployeeRepoistry employeeRepoistry;

    @Autowired
    public UserDetailsService(EmployeeRepoistry employeeRepoistry){
        this.employeeRepoistry = employeeRepoistry;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = employeeRepoistry.findByEmpEmail(username);
        if(user == null){
            return null;
        }
        return new UserDetailsDto(user);
    }
}
