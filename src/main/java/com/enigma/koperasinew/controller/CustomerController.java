package com.enigma.koperasinew.controller;
import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.response.CommonResponse;
import com.enigma.koperasinew.dto.response.CustomerResponse;
import com.enigma.koperasinew.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = AppPath.URL_CROSS)
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/v1/{id}")
    public ResponseEntity<CustomerResponse> updateCust(@PathVariable String id, @RequestBody CustomerRequest customerRequest) {
        customerRequest.setId(id);
        CustomerResponse updatedCust = customerService.updateCustomer(customerRequest);
        if (updatedCust != null) {
            return ResponseEntity.ok(updatedCust);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
