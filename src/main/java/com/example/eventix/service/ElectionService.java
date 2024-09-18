package com.example.eventix.service;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.dto.ElectionDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Election;
import com.example.eventix.repository.ElectionRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ElectionService {

    @Autowired
    private ElectionRepo electionRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveElection(ElectionDTO electionDTO) {

        try{
            if(electionRepo.existsById(electionDTO.getElection_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Election already exists");
                responseDTO.setContent(electionDTO);

            }else{
                Election savedElection =  electionRepo.save(modelMapper.map(electionDTO, Election.class));
                ElectionDTO savedElectionDTO = modelMapper.map(savedElection, ElectionDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Election Saved Successfully");
                responseDTO.setContent(savedElectionDTO);

            }

            return responseDTO;



        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }

    }

    public ResponseDTO getAllElections() {

        try{

            List<Election> electionList = electionRepo.findAll();
            if(!electionList.isEmpty()){
                List<ElectionDTO> electionDTOList = modelMapper.map(electionList, new TypeToken<List<ElectionDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Elections Successfully");
                responseDTO.setContent(electionDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Elections Found");
                responseDTO.setContent(null);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getElection(int election_id) {

        try{

            if(electionRepo.existsById(election_id)){
                Election election = electionRepo.findById(election_id).orElse(null);
                ElectionDTO electionDTO =  modelMapper.map(election, ElectionDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Election Retrieved Successfully");
                responseDTO.setContent(electionDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Election found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }

    }

    public ResponseDTO updateElection(int election_id, ElectionDTO electionDTO) {
        try{
            if(electionRepo.existsById(election_id)){
                Election updatedElection =  electionRepo.save(modelMapper.map(electionDTO, Election.class));
                ElectionDTO updatedElectionDTO = modelMapper.map(updatedElection, ElectionDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Election Updated Successfully");
                responseDTO.setContent(updatedElectionDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Election Found");
                responseDTO.setContent(null);

            }

            return responseDTO;



        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO deleteElection(int election_id) {

        try{

            if(electionRepo.existsById(election_id)){
                electionRepo.deleteById(election_id);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Election deleted Successfully");
                responseDTO.setContent(null);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Election Found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e);
            return responseDTO;
        }
    }

    public Optional<Election> findById(Integer electionId) {
        return electionRepo.findById(electionId);
    }

}
