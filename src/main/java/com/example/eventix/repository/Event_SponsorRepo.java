package com.example.eventix.repository;

import com.example.eventix.entity.Event_Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface Event_SponsorRepo extends JpaRepository<Event_Sponsor,Integer>, CrudRepository<Event_Sponsor,Integer> {

}
