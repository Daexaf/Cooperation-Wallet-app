package com.enigma.koperasinew.repository;

import com.enigma.koperasinew.constant.ERole;
import com.enigma.koperasinew.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
