package com.legaldocAnalyserV2.service;

import com.legaldocAnalyserV2.JWT.JwtUtil;
import com.legaldocAnalyserV2.model.User;
import com.legaldocAnalyserV2.repository.UserRepo;
import com.legaldocAnalyserV2.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepo repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthService(UserRepo repo,
                       BCryptPasswordEncoder encoder,
                       JwtUtil jwtUtil,AuthenticationManager authManager) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }
    public void register(String email, String password) {
        Optional<User> userCheck=repo.findByEmail(email);
        if(userCheck.isPresent()){
            throw new RuntimeException(" User Already Registered Please Login");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole("USER");
        repo.save(user);
    }

    public String login(String email, String password) {

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return jwtUtil.generateToken(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        );
    }
}
