package com.miatts.backend.common.appuser;

import com.miatts.backend.common.role.Role;
import com.miatts.backend.common.role.RoleRepository;
import com.miatts.backend.common.role.RoleType;
import com.miatts.exception.OperationNonPermittedException;
import com.miatts.util.ObjectsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final ObjectsValidator<AppUserRequest> validator;
    private final AppUserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Integer create(AppUserRequest request) {
        validator.validate(request);
        Boolean emailExists = appUserRepository.existsByEmail(request.getEmail());
        if (emailExists) {
            throw new OperationNonPermittedException("Email already exists");
        }





        var user = mapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));



        user.setRoles(request.getRoles());

        return appUserRepository.save(user).getId();
    }





}
