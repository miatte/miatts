package com.miatts.backend.common.appuser;

import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {
    public AppUser toUser(AppUserRequest request) {
        return AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

//    public UserResponse toResponse(User user) {
//        return UserResponse.builder()
//                .id(user.getId())
//                .firstname(user.getFirstname())
//                .lastname(user.getLastname())
//                .email(user.getEmail())
//                .active(user.isActive())
//                .iban(user.getAccount() == null ? null : user.getAccount().getIban())
//                .build();
//    }
}
