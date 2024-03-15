package com.enigma.koperasinew.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TransferResponse {
    private String id;
    private String senderId;
    private String recipientId;
    private String note;
    private double amount;
    private LocalDateTime date;
}
