package com.example.Portal.Services.ServiceImplementation;

import com.example.Portal.Models.RefreshToken;
import com.example.Portal.Repoistry.RefreshTokenRepo;
import com.example.Portal.Repoistry.EmployeeRepoistry;
import com.example.Portal.Services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepo refreshTokenRepo;
    EmployeeRepoistry employeeRepoistry;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepo refreshTokenRepo, EmployeeRepoistry employeeRepoistry) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.employeeRepoistry = employeeRepoistry;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();
        if(refreshTokenRepo.findByEmployee(employeeRepoistry.findByEmpId(userId))==null) {
            refreshToken.setEmployee(employeeRepoistry.findByEmpId(userId));
            long refreshTokenDurationMs = 922332036L;
            refreshToken.setExpiryDate(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepo.save(refreshToken);
            return refreshToken;
        }
        else{
            return refreshTokenRepo.findByEmployee(employeeRepoistry.findByEmpId(userId));
        }
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepo.delete(token);
            return null;
        }

        return token;
    }
}