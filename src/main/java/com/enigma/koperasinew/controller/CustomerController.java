package com.enigma.koperasinew.controller;
import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.response.CommonResponse;
import com.enigma.koperasinew.dto.response.CustomerResponse;
import com.enigma.koperasinew.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/v1")
    public ResponseEntity<CommonResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<CustomerResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new Customer")
                        .data(customerResponse)
                        .build());
    }

    @GetMapping("/v1")
    public ResponseEntity<?> getAllCustomer() {
        List<CustomerResponse> customerList = customerService.getAllCustomers();

        return ResponseEntity.ok(
                CommonResponse.<List<CustomerResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully retrieved all customer")
                        .data(customerList)
                        .build());
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<CustomerResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get customer by id")
                        .data(customerResponse)
                        .build());
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable String id) {
        customerService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Delete Customer")
                        .data(HttpStatus.OK)
                        .build());
    }
}
