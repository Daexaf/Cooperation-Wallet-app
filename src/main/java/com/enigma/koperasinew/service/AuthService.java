package com.enigma.koperasinew.service;

import com.enigma.koperasinew.dto.request.AdminRequest;
import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.request.LoginRequest;
import com.enigma.koperasinew.dto.response.LoginResponse;
import com.enigma.koperasinew.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(CustomerRequest request);

    LoginResponse login(LoginRequest authRequest);

    RegisterResponse registerAdmin(AdminRequest request);
}
