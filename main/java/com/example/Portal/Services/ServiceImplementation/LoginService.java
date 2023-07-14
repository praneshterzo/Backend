package com.example.Portal.Services.ServiceImplementation;

import com.example.Portal.Dto.AuthenticationResponseDto;
import com.example.Portal.Dto.LoginDto;
import com.example.Portal.Models.Employee;
import com.example.Portal.Repoistry.EmployeeRepoistry;
import com.example.Portal.Services.RefreshTokenService;
import com.example.Portal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    JwtUtils jwtUtils;

    UserDetailsService userDetailsService;
    EmployeeRepoistry employeeRepoistry;

    AuthenticationManager authenticationManager;

    RefreshTokenService refreshTokenService;

    @Autowired
    public LoginService(JwtUtils jwtUtils, UserDetailsService userDetailsService, EmployeeRepoistry employeeRepoistry, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.employeeRepoistry = employeeRepoistry;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthenticationResponseDto authenticate(LoginDto loginDTO){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            Employee employee = employeeRepoistry.findByEmpEmail(loginDTO.getEmail());
            System.out.println(employee.getEmpId());
            return AuthenticationResponseDto
                    .builder()
                    .jwt(jwtUtils.generateJwt(loginDTO.getEmail()))
                    .refreshToken(refreshTokenService.createRefreshToken(employee.getEmpId()).getToken())
                    .empId(employee.getEmpId())
                    .build();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
