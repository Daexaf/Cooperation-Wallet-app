package com.enigma.koperasinew.service.impl;

import com.enigma.koperasinew.dto.request.TransferRequest;
import com.enigma.koperasinew.dto.response.TransferResponse;
import com.enigma.koperasinew.service.TransferService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final EntityManager entityManager;

    @Override
    public TransferResponse transfer(TransferRequest transferRequest) {
        String newTransferId = UUID.randomUUID().toString();
        int updatedSenderRows = entityManager.createNativeQuery(
                        "UPDATE m_saving SET amount = amount - ? WHERE id = ? AND amount >= ?")
                .setParameter(1, transferRequest.getAmount())
                .setParameter(2, transferRequest.getSenderId())
                .setParameter(3, transferRequest.getAmount())
                .executeUpdate();

        if (updatedSenderRows == 0) {
            throw new IllegalArgumentException("Insufficient balance in sender account for transfer");
        }

        int updatedRecipientRows = entityManager.createNativeQuery(
                        "UPDATE m_saving SET amount = amount + ? WHERE id = ?")
                .setParameter(1, transferRequest.getAmount())
                .setParameter(2, transferRequest.getRecipientId())
                .executeUpdate();

        if (updatedRecipientRows == 0) {
            throw new IllegalArgumentException("Recipient account not found");
        }

        entityManager.createNativeQuery(
                        "INSERT INTO trx_transfer (id, sender_id, recipient_id, note, amount, transfer_date) " +
                                "VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, newTransferId)
                .setParameter(2, transferRequest.getSenderId())
                .setParameter(3, transferRequest.getRecipientId())
                .setParameter(4, transferRequest.getNote())
                .setParameter(5, transferRequest.getAmount())
                .setParameter(6, LocalDateTime.now())
                .executeUpdate();
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setId(newTransferId);
        transferResponse.setSenderId(transferRequest.getSenderId());
        transferResponse.setRecipientId(transferRequest.getRecipientId());
        transferResponse.setNote(transferRequest.getNote());
        transferResponse.setAmount(transferRequest.getAmount());
        return transferResponse;
    }

    @Override
    public List<TransferResponse> getAllTransfers() {
        List<Object[]> resultList = entityManager.createNativeQuery(
                        "SELECT id, sender_id, recipient_id, note, amount, transfer_date FROM trx_transfer")
                .getResultList();

        return resultList.stream()
                .map(row -> {
                    String id = (String) row[0];
                    String senderId = (String) row[1];
                    String recipientId = (String) row[2];
                    String note = (String) row[3];
                    Double amount = (Double) row[4];
                    Timestamp transferDateTimestamp = (Timestamp) row[5];
                    LocalDateTime transferDate = transferDateTimestamp.toLocalDateTime();
                    return TransferResponse.builder()
                            .id(id)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .note(note)
                            .amount(amount)
                            .date(transferDate)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public TransferResponse getTransferById(String id) {
        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, sender_id, recipient_id, note, amount, transfer_date FROM trx_transfer WHERE id = ?")
                .setParameter(1, id)
                .getSingleResult();
        if (result != null) {
            String transferId = (String) result[0];
            String senderId = (String) result[1];
            String recipientId = (String) result[2];
            String note = (String) result[3];
            Double amount = (Double) result[4];
            Timestamp transferDateTimestamp = (Timestamp) result[5];
            LocalDateTime transferDate = transferDateTimestamp.toLocalDateTime();

            return TransferResponse.builder()
                    .id(transferId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .note(note)
                    .amount(amount)
                    .date(transferDate)
                    .build();
        }
        return null;
    }

}
