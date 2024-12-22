package com.miatts.util;

import com.miatts.backend.common.appuser.AppUser;
import com.miatts.backend.common.appuser.AppUserRepository;
import com.miatts.backend.common.role.Role;
import com.miatts.backend.common.role.RoleRepository;
import com.miatts.backend.common.role.RoleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration

@RequiredArgsConstructor

public class InitDB {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void insertAdminUser() {
//        log.info("Insert ADMIN user for QA purposes");
        List<RoleType> list = Arrays.asList(RoleType.values());
        list.forEach(role -> {
            roleRepository.save(
                    Role.builder()
                            .name(role.name())
//                            .isDefault(role.name().equals("ROLE_USER") ? true : false)
                            .build()
            );
        });

        List<Role> allRoles = roleRepository.findAll();
        Optional<Role> role = roleRepository.findByName(RoleType.ROLE_ADMIN.name());


        userRepository.save(
                AppUser.builder()
                        .firstName("ali")
                        .lastName("bouali")
                        .email("admin@alibou.com")
                        .password("$2a$10$3ek/PrpenD0.ppuO7Jp7HulcpTqpcUrETeKbIVwUrU139n5Co3nZO")
                        .roles(allRoles)
                        .build()
        );



    }
}
