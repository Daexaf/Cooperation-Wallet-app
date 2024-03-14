package com.enigma.koperasinew.service;

import com.enigma.koperasinew.dto.request.AdminRequest;
import com.enigma.koperasinew.dto.response.AdminResponse;
import com.enigma.koperasinew.entity.Admin;

import java.util.List;

public interface AdminService {

    AdminResponse createAdmin(AdminRequest authRequest);

    AdminResponse createAdmins(Admin request);

    AdminResponse update(AdminRequest authRequest);

    void delete(String id);

    List<AdminResponse> getAll();

    AdminResponse getById(String id);
}
