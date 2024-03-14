package com.enigma.koperasinew.repository;

import com.enigma.koperasinew.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingRepository extends JpaRepository<Saving, String> {

}
