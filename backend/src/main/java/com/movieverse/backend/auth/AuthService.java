package com.movieverse.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(AppUserRepository userRepository, PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public AuthResponse register(AuthRequest request) {
        String username = request.username().trim();
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        AppUser user = new AppUser(username, passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new AuthResponse(tokenService.createToken(username), username);
    }

    public AuthResponse login(AuthRequest request) {
        AppUser user = userRepository.findByUsername(request.username().trim())
                .orElseThrow(AuthService::badLogin);
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw badLogin();
        }
        return new AuthResponse(tokenService.createToken(user.getUsername()), user.getUsername());
    }

    public AppUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    // Same message for "wrong username" and "wrong password", so attackers can't guess usernames
    private static ResponseStatusException badLogin() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
    }
}
