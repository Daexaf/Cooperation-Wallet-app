package com.enigma.koperasinew.service.impl;

import com.enigma.koperasinew.dto.request.AdminRequest;
import com.enigma.koperasinew.dto.response.AdminResponse;
import com.enigma.koperasinew.entity.Admin;
import com.enigma.koperasinew.service.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class AdminServiceImpl implements AdminService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        String newAdminId = UUID.randomUUID().toString();

        entityManager.createNativeQuery(
                        "INSERT INTO m_admin (id, full_name, mobile_phone, email) " +
                                "VALUES (?, ?, ?, ?)")
                .setParameter(1, newAdminId)
                .setParameter(2, adminRequest.getName())
                .setParameter(3, adminRequest.getMobilePhone())
                .setParameter(4, adminRequest.getEmail())
                .executeUpdate();
        Admin newAdmin = entityManager.find(Admin.class, newAdminId);
        return AdminResponse.builder()
                .id(newAdmin.getId())
                .name(newAdmin.getName())
                .mobilePhone(newAdmin.getMobilePhone())
                .email(newAdmin.getEmail())
                .build();
    }

    @Override
    public AdminResponse createAdmins(Admin adminRequest) {
        String newAdminId = UUID.randomUUID().toString();

        entityManager.createNativeQuery(
                        "INSERT INTO m_admin (id, full_name, mobile_phone, email,user_credential_id) " +
                                "VALUES (?, ?, ?, ?,?)")
                .setParameter(1, newAdminId)
                .setParameter(2, adminRequest.getName())
                .setParameter(3, adminRequest.getMobilePhone())
                .setParameter(4, adminRequest.getEmail())
                .setParameter(5,adminRequest.getUserCredential().getId())
                .executeUpdate();
        Admin newAdmin = entityManager.find(Admin.class, newAdminId);
        return AdminResponse.builder()
                .id(newAdmin.getId())
                .name(newAdmin.getName())
                .mobilePhone(newAdmin.getMobilePhone())
                .email(newAdmin.getEmail())
                .build();
    }

    @Override
    public AdminResponse update(AdminRequest adminRequest) {
        Query findAdminQuery = entityManager.createNativeQuery(
                        "SELECT * FROM m_admin WHERE id = ?", Admin.class)
                .setParameter(1, adminRequest.getId());
        List<Admin> resultList = findAdminQuery.getResultList();
        if (!resultList.isEmpty()) {
            Admin existingAdmin = resultList.get(0);
            Query updateQuery = entityManager.createNativeQuery(
                            "UPDATE m_admin SET full_name = ?, mobile_phone = ?, email = ? WHERE id = ?")
                    .setParameter(1, adminRequest.getName())
                    .setParameter(2, adminRequest.getMobilePhone())
                    .setParameter(3, adminRequest.getEmail())
                    .setParameter(4, adminRequest.getId());
            updateQuery.executeUpdate();
            return AdminResponse.builder()
                    .id(adminRequest.getId())
                    .name(adminRequest.getName())
                    .mobilePhone(adminRequest.getMobilePhone())
                    .email(adminRequest.getEmail())
                    .build();
        } else {
            return null;
        }
    }


    @Override
    public void delete(String id) {
        int deletedRows = entityManager.createNativeQuery(
                        "DELETE FROM m_admin WHERE id = ?")
                .setParameter(1, id)
                .executeUpdate();
        if (deletedRows > 0) {
            System.out.println("delete succeed");
        } else {
            System.out.println("id not found");
        }
    }

    @Override
    public List<AdminResponse> getAll() {
        List<Object[]> resultList = entityManager.createNativeQuery(
                        "SELECT id, full_name, mobile_phone, email, FROM am_admin")
                .getResultList();
        return resultList.stream()
                .map(row -> AdminResponse.builder()
                        .id((String) row[0])
                        .name((String) row[1])
                        .mobilePhone((String) row[2])
                        .email((String) row[3])
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponse getById(String id) {
        Object[] result = (Object[]) entityManager.createNativeQuery(
                        "SELECT id, full_name, mobile_phone, email FROM m_admin WHERE id = ?")
                .setParameter(1, id)
                .getSingleResult();
        if (result != null) {
            return AdminResponse.builder()
                    .id((String) result[0])
                    .name((String) result[1])
                    .mobilePhone((String) result[2])
                    .email((String) result[3])
                    .build();
        }
        return null;
    }
}
