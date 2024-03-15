package com.enigma.koperasinew.service;

import com.enigma.koperasinew.dto.request.SavingRequest;
import com.enigma.koperasinew.dto.response.SavingResponse;

import java.util.List;

public interface SavingService {

    List<SavingResponse> getAllSavings();

    SavingResponse createSaving(SavingRequest savingRequest);

    void delete(String id);

    SavingResponse getSavingById(String id);

    SavingResponse deposit(String id, double addAmount);

    SavingResponse withdraw(String id, double withdrawAmount);

    SavingResponse getSavingByIdCust(String custId);
}
