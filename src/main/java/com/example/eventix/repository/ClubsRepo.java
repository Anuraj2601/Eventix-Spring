package com.example.eventix.repository;

import com.example.eventix.entity.Clubs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClubsRepo extends CrudRepository<Clubs, Integer>, JpaRepository<Clubs, Integer> {
}