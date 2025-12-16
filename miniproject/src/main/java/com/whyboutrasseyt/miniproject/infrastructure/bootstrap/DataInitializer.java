package com.whyboutrasseyt.miniproject.infrastructure.bootstrap;

import com.whyboutrasseyt.miniproject.domain.model.User;
import com.whyboutrasseyt.miniproject.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findByEmail("boutrasseytwassim@gmail.com").orElseGet(() ->
                userRepository.save(new User(
                        null,
                        "boutrasseytwassim@gmail.com",
                        passwordEncoder.encode("password"),
                        "USER"
                ))
        );
    }
}
