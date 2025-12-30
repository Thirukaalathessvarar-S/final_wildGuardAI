package com.wildlife.rescue.controller;

import com.wildlife.rescue.model.User;
import com.wildlife.rescue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/vets/available")
    public List<User> getAvailableVets() {
        return userService.getAvailableVets();
    }
}
