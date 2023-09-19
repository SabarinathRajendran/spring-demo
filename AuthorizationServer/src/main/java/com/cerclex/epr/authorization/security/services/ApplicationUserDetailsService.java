package com.cerclex.epr.authorization.security.services;

import com.cerclex.epr.authorization.dtos.ApplicationUsers;
import com.cerclex.epr.authorization.dtos.User;
import com.cerclex.epr.authorization.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersService usersService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //return new ApplicationUsers(usersService.getByUsrName(s).orElseThrow(() -> new UsernameNotFoundException("Username Not Found")));

        Optional<User> userOptional = usersService.getByUsrName(s);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException("Username not found");
        User user = userOptional.get();
        if (!user.getBrand().isActive() || !user.getBrand().isEnabled()) throw new UsernameNotFoundException("User not active, Please contact admin");
        return new ApplicationUsers(user);
    }
}
