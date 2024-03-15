package com.enigma.koperasinew.service;

import com.enigma.koperasinew.dto.request.TransferRequest;
import com.enigma.koperasinew.dto.response.TransferResponse;

import java.util.List;

public interface TransferService {
    TransferResponse transfer(TransferRequest transferRequest);

    TransferResponse getTransferById(String id);

    List<TransferResponse> getAllTransfers();
}
