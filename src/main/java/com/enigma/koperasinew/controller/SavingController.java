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

@CrossOrigin(origins = AppPath.URL_CROSS)
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/v1")
    public ResponseEntity<List<SavingResponse>> getAllSavings() {
        List<SavingResponse> savings = savingService.getAllSavings();
        return ResponseEntity.ok()
                .body(savings);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/v1/{id}")
    public ResponseEntity<SavingResponse> getSavingById(@PathVariable String id) {
        SavingResponse response = savingService.getSavingById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/v1/{id}/deposit")
    public ResponseEntity<SavingResponse> deposit(@PathVariable String id, @RequestParam double addAmount) {
        SavingResponse response = savingService.deposit(id, addAmount);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/v1/{id}/withdraw")
    public ResponseEntity<SavingResponse> withdraw(@PathVariable String id, @RequestParam double withdrawAmount) {
        SavingResponse response = savingService.withdraw(id, withdrawAmount);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<String> deleteSaving(@PathVariable String id) {
        savingService.delete(id);
        return ResponseEntity.ok("Data berhasil dihapus");
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/v1/cust/{id}")
    public ResponseEntity<SavingResponse> getSavingByIdCust(@PathVariable String id) {
        SavingResponse response = savingService.getSavingByIdCust(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
