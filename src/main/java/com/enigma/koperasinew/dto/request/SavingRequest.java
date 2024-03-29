package com.enigma.koperasinew.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SavingRequest {
    private String id;
    private String name;
    private String customer;
    private String type;
    private LocalDateTime date;
    private Double amount;
}
