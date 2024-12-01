package com.example.eventix.controller;

import com.example.eventix.dto.BudgetDTO;
import com.example.eventix.dto.EventRegistrationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "https://eventix-18.netlify.app")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;


    @GetMapping("/getAllBudgets")
    public ResponseEntity<ResponseDTO> getAllBudgets() {
        return ResponseEntity.ok().body(budgetService.getAllBudgets());
    }

    @GetMapping("/getBudget/{budgetId}")
    public ResponseEntity<ResponseDTO> getBudget(@PathVariable int budgetId){
        return ResponseEntity.ok().body(budgetService.getBudget(budgetId));
    }

    @PostMapping(value ="/saveBudget", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> saveBudget(@RequestBody BudgetDTO budgetDTO){
        //return ResponseEntity.ok(announcementService.saveAnnouncement(announcementDTO));
        return ResponseEntity.ok().body(budgetService.saveBudget(budgetDTO));
    }

    @PutMapping(value ="/updateBudget/{budgetId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateBudget(@PathVariable int budgetId, @RequestBody BudgetDTO budgetDTO){
        return ResponseEntity.ok().body(budgetService.updateBudget(budgetId, budgetDTO));
    }

    @DeleteMapping("/deleteBudget/{budgetId}")
    public ResponseEntity<ResponseDTO> deleteBudget(@PathVariable int budgetId){
        return ResponseEntity.ok().body(budgetService.deleteBudget(budgetId));
    }





}
