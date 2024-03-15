package com.enigma.koperasinew.controller;

import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.SavingRequest;
import com.enigma.koperasinew.dto.response.CommonResponse;
import com.enigma.koperasinew.dto.response.SavingResponse;
import com.enigma.koperasinew.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.SAVING)
@RequiredArgsConstructor
public class SavingController {
    private final SavingService savingService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/v1")
    public ResponseEntity<CommonResponse> createSaving(@RequestBody SavingRequest savingRequest) {
        SavingResponse savingResponse = savingService.createSaving(savingRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<SavingResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new Saving")
                        .data(savingResponse)
                        .build());
    }

    @GetMapping("/v1")
    public ResponseEntity<List<SavingResponse>> getAllSavings() {
        List<SavingResponse> savings = savingService.getAllSavings();
        return ResponseEntity.ok(savings);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<SavingResponse> getSavingById(@PathVariable String id) {
        SavingResponse response = savingService.getSavingById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/v1/{id}/addAmount")
    public ResponseEntity<SavingResponse> addAmount(@PathVariable String id, @RequestParam double addAmount) {
        SavingResponse response = savingService.addAmount(id, addAmount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/{id}/withdraw")
    public ResponseEntity<SavingResponse> withdraw(@PathVariable String id, @RequestParam double withdrawAmount) {
        SavingResponse response = savingService.withdraw(id, withdrawAmount);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaving(@PathVariable String id) {
        savingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
