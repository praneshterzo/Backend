package com.example.Portal.Services;

import com.example.Portal.Models.Login;
import com.example.Portal.Models.Role;

public interface Loginservices {
    public void save(Login login);
    Role getRoleById(int id);

    Role findByName(String name);
}
