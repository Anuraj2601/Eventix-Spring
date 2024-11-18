package com.example.eventix.repository;

import com.example.eventix.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BudgetRepo extends JpaRepository<Budget, Integer> {
}
