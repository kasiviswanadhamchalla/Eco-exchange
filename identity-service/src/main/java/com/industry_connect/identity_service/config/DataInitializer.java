package com.industry_connect.identity_service.config;

import com.industry_connect.identity_service.entity.User;
import com.industry_connect.identity_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("platformadmin@ecoexchange.com").isEmpty()) {
            User admin = new User(
                    null,
                    "Platform Admin",
                    "platformadmin@ecoexchange.com",
                    passwordEncoder.encode("Password123!"),
                    "PLATFORM_ADMIN",
                    "ACTIVE"
            );
            userRepository.save(admin);
        }
    }
}
