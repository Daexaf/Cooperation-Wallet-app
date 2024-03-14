package com.enigma.koperasinew.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "full_name", nullable = false, length = 100)
    private String name;
    @Column(name = "mobile_phone", nullable = false, unique = true ,length = 15)
    private String mobilePhone;
    @Column(name = "email", nullable = false, length = 50)
    private String email;
    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}
