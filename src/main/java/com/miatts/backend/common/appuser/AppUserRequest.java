package com.miatts.backend.common.appuser;

import com.miatts.backend.common.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserRequest {

    @NotNull(message = "First name is mandatory")
    private String firstName;
    @NotNull(message = "Last name is mandatory")
    private String lastName;
    @NotNull(message = "Email name is mandatory")
    @Email(message = "Email is not valid")
    private String email;
    @NotNull(message = "Email name is mandatory")
    @Size(min = 4, max = 16, message = "Password should be between 4 and 16 chars")
    private String password;
    private List<Role> roles;
}
