package com.enigma.koperasinew.service;

import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> getAllCustomers();

    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse updateCustomer(CustomerRequest customerRequest);

    void delete(String id);

    CustomerResponse getCustomerById(String id);
}
