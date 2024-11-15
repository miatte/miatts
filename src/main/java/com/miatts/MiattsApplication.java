package com.miatts;

import com.miatts.auth.AuthenticationService;
import com.miatts.auth.RegisterRequest;
import com.miatts.backend.common.appuser.AppUser;
import com.miatts.backend.common.appuser.AppUserRepository;
import com.miatts.backend.common.appuser.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiattsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiattsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authenticationService, AppUserRepository userRepository) {
        return args -> {

            var admin = RegisterRequest.builder()
                    .email("admin@workshop.com")
                    .password("password")
                    .firstName("admin")
                    .lastName("workshop")
                    .role(Role.ADMIN)
                    .build();
            System.out.println("Admin Token: " + authenticationService.register(admin).getAccessToken());

            AppUser user = userRepository.findByEmail("admin@workshop.com").get();

            //TODO testing
            var manager = RegisterRequest.builder()
                    .email("manager@workshop.com")
                    .password("password")
                    .firstName("manager")
                    .lastName("workshop")
                    .role(Role.MANAGER)
                    .build();
            System.out.println("Manager token: " + authenticationService.register(manager).getAccessToken());






        };
    }

}
