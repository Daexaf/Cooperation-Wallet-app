package com.enigma.koperasinew.service.impl;

import com.enigma.koperasinew.constant.ERole;
import com.enigma.koperasinew.dto.request.AdminRequest;
import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.request.LoginRequest;
import com.enigma.koperasinew.dto.response.LoginResponse;
import com.enigma.koperasinew.dto.response.RegisterResponse;
import com.enigma.koperasinew.entity.*;
import com.enigma.koperasinew.repository.UserCredentialRepository;
import com.enigma.koperasinew.security.JwtUtil;
import com.enigma.koperasinew.service.AdminService;
import com.enigma.koperasinew.service.AuthService;
import com.enigma.koperasinew.service.CustomerService;
import com.enigma.koperasinew.service.RoleService;
import com.enigma.koperasinew.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerCustomer(CustomerRequest request) {
        try {
            validationUtil.validate(request);
            System.out.println("Received username: " + request.getUsername());

            if (request.getUsername() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null");
            }
            //todo 2: set role
            Role role = Role.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build();
            Role roleSaved = roleService.getOrSave(role);
            //todo 1: set credential
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(roleSaved)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //todo 3: set customer
            Customer customer = Customer.builder()
                    .userCredential(userCredential)
                    .name(request.getName())
                    .address(request.getAddress())
                    .mobilePhone(request.getMobilePhone())
                    .email(request.getEmail())
                    .customerIdentity(request.getCustomerIdentity())
                    .build();
            customerService.createNewCustomer(customer);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
        }
    }

    @Override
    public LoginResponse login(LoginRequest authRequest) {
        try {
            validationUtil.validate(authRequest);

            String username = authRequest.getUsername();
            if (username != null) {
                username = username.toLowerCase();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null");
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    authRequest.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AppUser appUser = (AppUser) authentication.getPrincipal();
            String token = jwtUtil.generateToken(appUser);

            return LoginResponse.builder()
                    .username(appUser.getUsername())
                    .token(token)
                    .role(appUser.getRole().name())
                    .build();
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be null");
        }
    }

    @Override
    public RegisterResponse registerAdmin(AdminRequest request) {
        try {
            validationUtil.validate(request);
            //todo 2: set role
            Role role = Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build();
            Role roleSaved = roleService.getOrSave(role);
            //todo 1: set credential
            UserCredential userCredential = UserCredential.builder()
                    .username(request.getUsername().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(roleSaved)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //todo 3: set customer
            Admin admin = Admin.builder()
                    .userCredential(userCredential)
                    .name(request.getName())
                    .mobilePhone(request.getMobilePhone())
                    .email(request.getEmail())
                    .build();
            adminService.createAdmins(admin);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exist");
        }
    }
}
