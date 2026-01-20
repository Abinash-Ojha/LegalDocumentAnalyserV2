package com.legaldocAnalyserV2.controller;

import com.legaldocAnalyserV2.DTO.AuthRequest;
import com.legaldocAnalyserV2.model.User;
import com.legaldocAnalyserV2.repository.UserRepo;
import com.legaldocAnalyserV2.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    private final UserRepo userRepo;

    public AuthController(AuthService service,UserRepo userRepo) {
        this.service = service;
        this.userRepo=userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        Optional<User> user=userRepo.findByEmail(request.getEmail());
        if(user.isPresent()){
           return ResponseEntity.badRequest().body("User Already Exist");
        }

        service.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("User Registered Sucessfully");
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        return service.login(request.getEmail(), request.getPassword());
    }
}
