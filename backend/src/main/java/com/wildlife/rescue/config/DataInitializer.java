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
        if (userRepository.findByUsername("coordinator_user").isEmpty()) {
            User user = new User();
            user.setUsername("coordinator_user");
            user.setPassword("dummy_password");
            user.setRole("coordinator");
            userRepository.save(user);
        }
        if (userRepository.findByUsername("vet_user").isEmpty()) {
            User user = new User();
            user.setUsername("vet_user");
            user.setPassword("dummy_password");
            user.setRole("vet");
            userRepository.save(user);
        }
        if (userRepository.findByUsername("admin_user").isEmpty()) {
            User user = new User();
            user.setUsername("admin_user");
            user.setPassword("dummy_password");
            user.setRole("admin");
            userRepository.save(user);
        }

        // Add Indianized vet names
        if (userRepository.findByUsername("priya_sharma").isEmpty()) {
            User user = new User();
            user.setUsername("priya_sharma");
            user.setPassword("dummy_password");
            user.setRole("vet");
            user.setAvailable(true);
            userRepository.save(user);
        }

        if (userRepository.findByUsername("rohan_gupta").isEmpty()) {
            User user = new User();
            user.setUsername("rohan_gupta");
            user.setPassword("dummy_password");
            user.setRole("vet");
            user.setAvailable(true);
            userRepository.save(user);
        }

        if (userRepository.findByUsername("anjali_verma").isEmpty()) {
            User user = new User();
            user.setUsername("anjali_verma");
            user.setPassword("dummy_password");
            user.setRole("vet");
            user.setAvailable(true);
            userRepository.save(user);
        }

        if (userRepository.findByUsername("vikram_singh").isEmpty()) {
            User user = new User();
            user.setUsername("vikram_singh");
            user.setPassword("dummy_password");
            user.setRole("vet");
            user.setAvailable(true);
            userRepository.save(user);
        }
    }
}
