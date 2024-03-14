package com.enigma.koperasinew.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "m_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "full_name", nullable = false, length = 100)
    private String name;
    @Column(name = "mobile_phone", nullable = false, unique = true ,length = 15)
    private String mobilePhone;
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @Column(name = "address", nullable = false, length = 100)
    private String address;
    @Column(name = "customer_identity", nullable = false, length = 16, unique = true)
    private String customerIdentity;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

    public Customer(String id, String name, String mobilePhone, String address, String email, String customerIdentity) {
    }
}
