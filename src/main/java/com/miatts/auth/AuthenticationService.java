package com.miatts.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miatts.backend.common.role.Role;
import com.miatts.backend.common.role.RoleRepository;
import com.miatts.backend.common.role.RoleType;
import com.miatts.util.ObjectsValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.miatts.backend.common.appuser.AppUser;
import com.miatts.backend.common.appuser.AppUserRepository;
import com.miatts.backend.common.token.Token;
import com.miatts.backend.common.token.TokenRepository;
import com.miatts.backend.common.token.TokenType;
import com.miatts.config.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final ObjectsValidator<RegisterRequest> validator;


    public AuthenticationResponse register(RegisterRequest registerRequest) {
        validator.validate(registerRequest);
        var user = AppUser.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        var userRole = roleRepository.findByName(RoleType.ROLE_USER.name())
                .orElse(
                        Role.builder()
                                .name(RoleType.ROLE_USER.name())
                                .build()
                );

//        var userRole2 = roleRepository.findByName(RoleType.ROLE_ADMIN.name())
//                .orElse(
//                        Role.builder()
//                                .name(RoleType.ROLE_ADMIN.name())
//                                .build()
//                );
//
//        if (userRole.getId() == null) {
//            userRole = roleRepository.save(userRole);
//        }
//        if (userRole2.getId() == null) {
//            userRole2 = roleRepository.save(userRole2);
//        }
//        var defaultUserRole = List.of(userRole, userRole2);

        user.setRoles(List.of(userRole));

        var savedUser = userRepository.save(user);

        userRole.setAppUsers(new ArrayList<>(List.of(savedUser)));
        roleRepository.save(userRole);
        var claims = new HashMap<String, Object>();
        claims.put("role", user.getRoles());
        claims.put("active", user.isEnabled());
        var jwtToken = jwtService.generateToken(savedUser, claims);
//        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getFirstName() + " " + user.getLastName())
                .userId(savedUser.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateRequest.getEmail(),
                        authenticateRequest.getPassword()
                )
        );



        var user = userRepository.findByEmail(authenticateRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var claims = new HashMap<String, Object>();
        claims.put("role", user.getRoles());
        claims.put("active", user.isEnabled());
        var jwtToken = jwtService.generateToken(user, claims);
//        revokeAllUserTokens(user);
//
//        saveUserToken(user, jwtToken);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getFirstName() + " " + user.getLastName())
                .userId(user.getId())
                .build();
    }


//        String jwtToken = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();

    private void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    private void saveUserToken(AppUser user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return;
//        }
//
//        refreshToken = authorizationHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//
//        if (userEmail != null) {
//            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }


}

