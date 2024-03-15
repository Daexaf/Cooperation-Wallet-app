package com.enigma.koperasinew.controller;

import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.AdminRequest;
import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.request.LoginRequest;
import com.enigma.koperasinew.dto.response.LoginResponse;
import com.enigma.koperasinew.dto.response.RegisterResponse;
import com.enigma.koperasinew.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse registerCustomer(@RequestBody CustomerRequest customerRequest){
        return authService.registerCustomer(customerRequest);
    }

    @PostMapping("/login")
    public LoginResponse loginCustomer(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/registerAdmin")
    public RegisterResponse registerAdmin(@RequestBody AdminRequest adminRequest){
        return authService.registerAdmin(adminRequest);
    }
}
