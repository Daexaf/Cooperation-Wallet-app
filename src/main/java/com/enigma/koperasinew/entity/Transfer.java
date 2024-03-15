package com.enigma.koperasinew.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "trx_transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "recipient_id", nullable = false)
    private String recipientId;

    @Column(name = "note")
    private String note;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "transfer_date", nullable = false)
    private LocalDateTime transferDate;
}
