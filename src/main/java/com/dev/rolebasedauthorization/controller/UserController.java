package com.dev.rolebasedauthorization.controller;

import com.dev.rolebasedauthorization.entity.User;
import com.dev.rolebasedauthorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private static final String[] ADMIN_ACCESS = {"ROLE_ADMIN", "ROLE_MODERATOR"};

    private static final String[] MODERATOR_ACCESS = {"ROLE_MODERATOR"};

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public String joinGroup(@RequestBody User user){
        user.setRoles(DEFAULT_ROLE);
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        repository.save(user);
        return "WelCome to group";
    }

    @GetMapping("/access/{userId}/{role}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String giveAccess(@PathVariable Long userId, @PathVariable String role, Principal principal){
        User user = repository.findById(userId).get();
        List<String> roleList = getUserRoles(principal);
        String newRole = "";

        if(roleList.contains(role)){
            newRole = user.getRoles()+", "+role;
            user.setRoles(newRole);
        }

        repository.save(user);
        return user.getUsername() + ", " + newRole + " has been assigned by " + principal.getName();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> loadUsers(){
        return repository.findAll();
    }


    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String testUserAccess(){
        return "Works~~";
    }
    private List<String> getUserRoles(Principal principal){
        String roles = getLoggedInUser(principal).getRoles();
        List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
        if(assignRoles.contains("ROLE_ADMIN")){
            return Arrays.stream(ADMIN_ACCESS).collect(Collectors.toList());
        }
        if(assignRoles.contains("ROLE_MODERATOR")){
            return Arrays.stream(MODERATOR_ACCESS).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private User getLoggedInUser(Principal principal){
        return repository.findByUsername(principal.getName()).get();
    }
}
