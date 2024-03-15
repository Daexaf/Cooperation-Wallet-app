package com.enigma.koperasinew.service.impl;

import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.response.CustomerResponse;
import com.enigma.koperasinew.entity.Customer;
import com.enigma.koperasinew.service.CustomerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CustomerServiceImpl implements CustomerService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Object[]> resultList = entityManager.createNativeQuery(
                        "SELECT id, full_name, mobile_phone, email, address, customer_identity FROM m_customer")
                .getResultList();

        return resultList.stream()
                .map(row -> CustomerResponse.builder()
                        .id((String) row[0])
                        .name((String) row[1])
                        .mobilePhone((String) row[2])
                        .email((String) row[3])
                        .address((String) row[4])
                        .customerIdentity((String) row[5])
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        String newCustomerId = UUID.randomUUID().toString();

        entityManager.createNativeQuery(
                        "INSERT INTO m_customer (id, full_name, mobile_phone, email, address, customer_identity) " +
                                "VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, newCustomerId)
                .setParameter(2, customerRequest.getName())
                .setParameter(3, customerRequest.getMobilePhone())
                .setParameter(4, customerRequest.getEmail())
                .setParameter(5, customerRequest.getAddress())
                .setParameter(6, customerRequest.getCustomerIdentity())
                .executeUpdate();

        Customer newCustomer = entityManager.find(Customer.class, newCustomerId);

        return CustomerResponse.builder()
                .id(newCustomer.getId())
                .name(newCustomer.getName())
                .mobilePhone(newCustomer.getMobilePhone())
                .email(newCustomer.getEmail())
                .address(newCustomer.getAddress())
                .customerIdentity(newCustomer.getCustomerIdentity())
                .build();
    }

    @Override
    public void delete(String id) {
        int deletedRows = entityManager.createNativeQuery(
                        "DELETE FROM m_customer WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();

        if (deletedRows > 0) {
            System.out.println("delete succeed");
        } else {
            System.out.println("id not found");
        }
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, full_name, mobile_phone, email, address, customer_identity FROM m_customer WHERE id = ?")
                .setParameter(1, id)
                .getSingleResult();

        if (result != null) {
            return CustomerResponse.builder()
                    .id((String) result[0])
                    .name((String) result[1])
                    .mobilePhone((String) result[2])
                    .email((String) result[3])
                    .address((String) result[4])
                    .customerIdentity((String) result[5])
                    .build();
        }
        return null;
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest) {
        Query findCustomerQuery = entityManager.createNativeQuery(
                        "SELECT * FROM m_customer WHERE id = ?", Customer.class)
                .setParameter(1, customerRequest.getId());

        List<Customer> resultList = findCustomerQuery.getResultList();
        if (!resultList.isEmpty()) {
            Customer existingCustomer = resultList.get(0);

            Query updateQuery = entityManager.createNativeQuery(
                            "UPDATE m_customer SET full_name = ?, mobile_phone = ?, email = ?, address = ?, customer_identity = ? WHERE id = ?")
                    .setParameter(1, customerRequest.getName())
                    .setParameter(2, customerRequest.getMobilePhone())
                    .setParameter(3, customerRequest.getEmail())
                    .setParameter(4, customerRequest.getAddress())
                    .setParameter(5, customerRequest.getCustomerIdentity())
                    .setParameter(6, customerRequest.getId());
            updateQuery.executeUpdate();

            return CustomerResponse.builder()
                    .id(customerRequest.getId())
                    .name(customerRequest.getName())
                    .mobilePhone(customerRequest.getMobilePhone())
                    .email(customerRequest.getEmail())
                    .address(customerRequest.getAddress())
                    .customerIdentity(customerRequest.getCustomerIdentity())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public CustomerResponse createNewCustomer(Customer customerRequest) {
        String newCustomerId = UUID.randomUUID().toString();

        entityManager.createNativeQuery(
                        "INSERT INTO m_customer (id, full_name, mobile_phone, email, address, customer_identity,user_credential_id) " +
                                "VALUES (?, ?, ?, ?, ?, ?,?)")
                .setParameter(1, newCustomerId)
                .setParameter(2, customerRequest.getName())
                .setParameter(3, customerRequest.getMobilePhone())
                .setParameter(4, customerRequest.getEmail())
                .setParameter(5, customerRequest.getAddress())
                .setParameter(6, customerRequest.getCustomerIdentity())
                .setParameter(7,customerRequest.getUserCredential().getId())
                .executeUpdate();

        Customer newCustomer = entityManager.find(Customer.class, newCustomerId);

        return CustomerResponse.builder()
                .id(newCustomer.getId())
                .name(newCustomer.getName())
                .mobilePhone(newCustomer.getMobilePhone())
                .email(newCustomer.getEmail())
                .address(newCustomer.getAddress())
                .customerIdentity(newCustomer.getCustomerIdentity())
                .build();
    }
}
