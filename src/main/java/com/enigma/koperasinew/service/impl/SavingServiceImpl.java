package com.enigma.koperasinew.service.impl;

import com.enigma.koperasinew.constant.EType;
import com.enigma.koperasinew.dto.request.SavingRequest;
import com.enigma.koperasinew.dto.response.SavingResponse;
import com.enigma.koperasinew.entity.Saving;
import com.enigma.koperasinew.service.SavingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class SavingServiceImpl implements SavingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SavingResponse> getAllSavings() {
        List<Object[]> resultList = entityManager.createNativeQuery(
                        "SELECT id, customer_id, e_type, date, amount FROM m_saving")
                .getResultList();

        return resultList.stream()
                .map(row -> {
                    String id = (String) row[0];
                    String customerId = (String) row[1];
                    EType eType = EType.valueOf((String) row[2]);
                    LocalDateTime date = ((Timestamp) row[3]).toLocalDateTime();
                    Double amount = (Double) row[4];

                    return SavingResponse.builder()
                            .id(id)
                            .customer(customerId)
                            .eType(eType)
                            .date(date)
                            .amount(amount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public SavingResponse createSaving(SavingRequest savingRequest) {
        String newSavingId = UUID.randomUUID().toString();
        double amount = savingRequest.getAmount();
        EType eType = EType.valueOf(savingRequest.getType());


        if (eType == EType.SILVER && amount < 50000) {
            throw new IllegalArgumentException("Minimum amount for Silver is 50000");
        } else if (eType == EType.GOLD && amount < 100000) {
            throw new IllegalArgumentException("Minimum amount for Gold is 100000");
        } else if (eType == EType.PLATINUM && amount < 150000) {
            throw new IllegalArgumentException("Minimum amount for Platinum is 150000");
        }

        double interestRate = eType.getInterestRate();
        double interest = amount * interestRate;

        entityManager.createNativeQuery(
                        "INSERT INTO m_saving (id, name ,customer_id, e_type, date, amount, interest) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, newSavingId)
                .setParameter(2, savingRequest.getName())
                .setParameter(3, savingRequest.getCustomer())
                .setParameter(4, savingRequest.getType())
                .setParameter(5, savingRequest.getDate())
                .setParameter(6, savingRequest.getAmount())
                .setParameter(7, interest)
                .executeUpdate();

        Saving newSaving = entityManager.find(Saving.class, newSavingId);

        return convertToResponse(newSaving);
    }

    @Override
    public void delete(String id) {
        int deletedRows = entityManager.createNativeQuery(
                        "DELETE FROM m_saving WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();

        if (deletedRows > 0) {
            System.out.println("Delete successful");
        } else {
            System.out.println("ID not found");
        }
    }

    @Override
    public SavingResponse getSavingById(String id) {
        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, name ,customer_id, e_type, date, amount, interest FROM m_saving WHERE id = ?")
                .setParameter(1, id)
                .getSingleResult();

        if (result != null) {
            return SavingResponse.builder()
                    .id((String) result[0])
                    .customer((String) result[1])
                    .eType(EType.valueOf((String) result[2]))
                    .date((LocalDateTime) result[3])
                    .amount((Double) result[4])
                    .build();
        }
        return null;
    }

    private SavingResponse convertToResponse(Saving saving) {
        if (saving != null) {
            return SavingResponse.builder()
                    .id(saving.getId())
                    .name(saving.getName())
                    .customer(saving.getCustomer().getId())
                    .eType(saving.getEType())
                    .date(saving.getDate())
                    .amount(saving.getAmount())
                    .interest(saving.getInterest())
                    .build();
        }
        return null;
    }

    @Override
    public SavingResponse addAmount(String id, double addAmount) {
        Saving existingSaving = entityManager.find(Saving.class, id);
        if (existingSaving == null) {
            throw new IllegalArgumentException("Saving with ID " + id + " not found");
        }
        double currentAmount = existingSaving.getAmount();
        double newAmount = currentAmount + addAmount;
        existingSaving.setAmount(newAmount);

        entityManager.merge(existingSaving);

        return convertToResponse(existingSaving);
    }

    @Override
    public SavingResponse withdraw(String id, double withdrawAmount) {
        Saving existingSaving = entityManager.find(Saving.class, id);
        if (existingSaving == null) {
            throw new IllegalArgumentException("Saving with ID " + id + " not found");
        }
        EType eType = existingSaving.getEType();
        double minimumBalance;
        switch (eType) {
            case SILVER:
                minimumBalance = 50000;
                break;
            case GOLD:
                minimumBalance = 75000;
                break;
            case PLATINUM:
                minimumBalance = 150000;
                break;
            default:
                throw new IllegalArgumentException("Invalid EType");
        }

        double currentAmount = existingSaving.getAmount();
        if (currentAmount - withdrawAmount < minimumBalance) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
        double newAmount = currentAmount - withdrawAmount;
        existingSaving.setAmount(newAmount);
        entityManager.merge(existingSaving);
        return convertToResponse(existingSaving);
    }
}
