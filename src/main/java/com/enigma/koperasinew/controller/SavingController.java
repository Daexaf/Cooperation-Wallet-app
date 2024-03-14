package com.enigma.koperasinew.controller;

import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.CustomerRequest;
import com.enigma.koperasinew.dto.request.SavingRequest;
import com.enigma.koperasinew.dto.response.CommonResponse;
import com.enigma.koperasinew.dto.response.CustomerResponse;
import com.enigma.koperasinew.dto.response.SavingResponse;
import com.enigma.koperasinew.service.SavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.SAVING)
@RequiredArgsConstructor
public class SavingController {
    private final SavingService savingService;

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
}
