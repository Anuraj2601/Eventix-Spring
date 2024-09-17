package com.example.eventix.repository;

import com.example.eventix.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepo extends JpaRepository<Registration, Integer> {
}
