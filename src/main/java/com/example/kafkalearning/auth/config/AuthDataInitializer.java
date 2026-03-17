package com.example.kafkalearning.auth.config;

import com.example.kafkalearning.auth.domain.AppUser;
import com.example.kafkalearning.auth.domain.UserRole;
import com.example.kafkalearning.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthDataInitializer {

    @Bean
    public CommandLineRunner adminUserInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.existsByUsername("admin")) {
                return;
            }

            AppUser adminUser = new AppUser();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPasswordHash(passwordEncoder.encode("Admin@123"));
            adminUser.setRole(UserRole.ADMIN);
            userRepository.save(adminUser);
        };
    }
}
