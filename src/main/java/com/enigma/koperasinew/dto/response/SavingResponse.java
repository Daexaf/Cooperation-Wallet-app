package com.enigma.koperasinew.dto.response;

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
public class SavingResponse {
    private String id;
    private String customer;
    private EType eType;
    private LocalDateTime date;
    private Double amount;
}
