package com.enigma.koperasinew.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerRequest {
    private String id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String mobilePhone;
    private String address;
    private String customerIdentity;
}
