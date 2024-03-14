package com.enigma.koperasinew.dto.request;

import com.enigma.koperasinew.constant.EType;
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
    private String customer;
    private String type;
    private LocalDateTime date;
    private Double amount;
}
