package com.enigma.koperasinew.repository;

import com.enigma.koperasinew.entity.Admin;
import com.enigma.koperasinew.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<UserCredential> findByUsername(String username);
}
