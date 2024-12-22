package com.miatts.backend.common.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;


    @PostMapping
    public Integer save(@RequestBody AppUserRequest appUserRequest) {
        return appUserService.create(appUserRequest);
    }



}
