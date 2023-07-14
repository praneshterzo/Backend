package com.example.Portal.Services;

import com.example.Portal.Models.*;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);

    RefreshToken createRefreshToken(int userId);

    RefreshToken verifyExpiration(RefreshToken token);

}