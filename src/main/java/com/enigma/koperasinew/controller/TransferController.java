package com.enigma.koperasinew.controller;

import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.TransferRequest;
import com.enigma.koperasinew.dto.response.CommonResponse;
import com.enigma.koperasinew.dto.response.TransferResponse;
import com.enigma.koperasinew.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = AppPath.URL_CROSS)
@RestController
@RequestMapping(AppPath.TRANSFER)
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/v1")
    public ResponseEntity<CommonResponse> transferAmount(@RequestBody TransferRequest transferRequest) {
        TransferResponse response = transferService.transfer(transferRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<TransferResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully transferred " + transferRequest.getAmount())
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/v1")
    public ResponseEntity<List<TransferResponse>> getAllTransfer() {
        List<TransferResponse> savings = transferService.getAllTransfers();
        return ResponseEntity.ok()
                .body(savings);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/v1/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable String id) {
        TransferResponse response = transferService.getTransferById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
