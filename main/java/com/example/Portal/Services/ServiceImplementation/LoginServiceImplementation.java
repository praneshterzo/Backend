package com.example.Portal.Services.ServiceImplementation;

import com.example.Portal.Models.Login;
import com.example.Portal.Models.Role;
import com.example.Portal.Repoistry.LoginRepoistry;
import com.example.Portal.Repoistry.RoleRepoistry;
import com.example.Portal.Services.Loginservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImplementation implements Loginservices {
    LoginRepoistry loginRepoistry;
    RoleRepoistry roleRepoistry;
    @Autowired
    public LoginServiceImplementation(LoginRepoistry loginRepoistry, RoleRepoistry roleRepoistry) {
        this.loginRepoistry = loginRepoistry;
        this.roleRepoistry=roleRepoistry;
    }

    @Override
    public void save(Login login) {
        loginRepoistry.save(login);
    }

    @Override
    public Role getRoleById(int id) {
        return roleRepoistry.findByRoleId(id);
    }

    @Override
    public Role findByName(String name) {
        return roleRepoistry.findByRoleName(name);
    }
}
