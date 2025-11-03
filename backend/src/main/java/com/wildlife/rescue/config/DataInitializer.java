package com.wildlife.rescue.config;

import com.wildlife.rescue.model.User;
import com.wildlife.rescue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("dummy_user").isEmpty()) {
            User dummyUser = new User();
            dummyUser.setUsername("dummy_user");
            dummyUser.setPassword("dummy_password");
            dummyUser.setRole("COORDINATOR");
            userRepository.save(dummyUser);
        }
    }
}
