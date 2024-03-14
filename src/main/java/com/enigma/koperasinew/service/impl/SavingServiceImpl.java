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
                        "SELECT id, customer_id, eType, date, amount FROM m_saving")
                .getResultList();

        return resultList.stream()
                .map(row -> SavingResponse.builder()
                        .id((String) row[0])
                        .customer((String) row[1])
                        .eType((EType) row[2])
                        .date((LocalDateTime) row[3])
                        .amount((Double) row[4])
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SavingResponse createSaving(SavingRequest savingRequest) {
        String newSavingId = UUID.randomUUID().toString();

        entityManager.createNativeQuery(
                        "INSERT INTO m_saving (id, customer_id, e_type, date, amount) " +
                                "VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, newSavingId)
                .setParameter(2, savingRequest.getCustomer())
                .setParameter(3, savingRequest.getType())
                .setParameter(4, savingRequest.getDate())
                .setParameter(5, savingRequest.getAmount())
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
                        "INSERT INTO m_saving (id, customer_id, e_type, date, amount) " +
                                "VALUES (?, ?, ?, ?, ?)")
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
                    .customer(saving.getCustomer().getId())
                    .eType(saving.getEType())
                    .date(saving.getDate())
                    .amount(saving.getAmount())
                    .build();
        }
        return null;
    }
}
