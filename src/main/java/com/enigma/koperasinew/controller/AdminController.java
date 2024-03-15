package com.enigma.koperasinew.controller;

import com.enigma.koperasinew.constant.AppPath;
import com.enigma.koperasinew.dto.request.AdminRequest;
import com.enigma.koperasinew.dto.response.AdminResponse;
import com.enigma.koperasinew.dto.response.CommonResponse;
import com.enigma.koperasinew.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = AppPath.URL_CROSS)
@RestController
@RequestMapping(AppPath.ADMIN)
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/v1")
    public ResponseEntity<CommonResponse> createAdmin(@RequestBody AdminRequest adminRequest) {
        AdminResponse adminResponse = adminService.createAdmin(adminRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<AdminResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully created new Admin")
                        .data(adminResponse)
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/v1")
    public ResponseEntity<?> getAllAdmin() {
        List<AdminResponse> adminList = adminService.getAll();
        return ResponseEntity.ok(
                CommonResponse.<List<AdminResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully retrieved all admin")
                        .data(adminList)
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable String id) {
        AdminResponse adminResponse = adminService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<AdminResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get admin by id")
                        .data(adminResponse)
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/v1/{id}")
    public ResponseEntity<?> deleteAdminById(@PathVariable String id) {
        adminService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Delete Admin")
                        .data(HttpStatus.OK)
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/v1/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable String id, @RequestBody AdminRequest adminRequest) {
        adminRequest.setId(id);
        AdminResponse updatedAdmin = adminService.update(adminRequest);
        if (updatedAdmin != null) {
            return ResponseEntity.ok(updatedAdmin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
