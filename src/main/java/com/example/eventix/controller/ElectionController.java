package com.example.eventix.controller;

import com.example.eventix.entity.Election;
import com.example.eventix.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class ElectionController {

    //@Autowired
    //private ElectionService electionService;

    @GetMapping("/getAllElections")
    public String getAllElections() {
        return "all elections";
    }

    @GetMapping("/getElection/{election_id}")
    public String getElection(@PathVariable int election_id) {
        return "get election";
    }

    @PostMapping("/saveElection")
    public String saveElection() {
        return "save election";
    }

    @PutMapping("/updateElection/{election_id}")
    public String updateElection() {
        return "update election";
    }

    @DeleteMapping("/deleteElection/{election_id}")
    public String deleteElection(@PathVariable int election_id) {
        return "delete election";
    }


}
