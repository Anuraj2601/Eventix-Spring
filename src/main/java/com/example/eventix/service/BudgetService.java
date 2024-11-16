package com.example.eventix.service;

import com.example.eventix.dto.BudgetDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Budget;
import com.example.eventix.repository.BudgetRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BudgetService {

    @Autowired
    private BudgetRepo budgetRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveBudget(BudgetDTO budgetDTO){
        try{
            if(budgetRepo.existsById(budgetDTO.getBudget_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Budget already exists");
                responseDTO.setContent(budgetDTO);

            }else{
                Budget savedBudget =  budgetRepo.save(modelMapper.map(budgetDTO, Budget.class));
                BudgetDTO savedBudgetDTO = modelMapper.map(savedBudget, BudgetDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Budget Saved Successfully");
                responseDTO.setContent(savedBudgetDTO);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }


    }

    public ResponseDTO getAllBudgets(){

        try{
            List<Budget> budgetList = budgetRepo.findAll();
            if(!budgetList.isEmpty()){
                List<BudgetDTO> budgetDTOList = modelMapper.map(budgetList, new TypeToken<List<BudgetDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Event Budgets Successfully");
                responseDTO.setContent(budgetDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Budget Found");
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

    public ResponseDTO getBudget(int budget_id){

        try{
            if(budgetRepo.existsById(budget_id)){
                Budget budget = budgetRepo.findById(budget_id).orElse(null);
                BudgetDTO budgetDTO =  modelMapper.map(budget, BudgetDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Budget Retrieved Successfully");
                responseDTO.setContent(budgetDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Budget found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        }catch (Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }

    }


    public ResponseDTO updateBudget(int budget_id, BudgetDTO budgetDTO){
        try{
            if(budgetRepo.existsById(budget_id)){
                Budget updatedBudget =  budgetRepo.save(modelMapper.map(budgetDTO, Budget.class));
                BudgetDTO updatedBudgetDTO = modelMapper.map(updatedBudget, BudgetDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Budget Updated Successfully");
                responseDTO.setContent(updatedBudgetDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Budget Found");
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


    public ResponseDTO deleteBudget(int budget_id){
        try{
            if(budgetRepo.existsById(budget_id)){
                budgetRepo.deleteById(budget_id);
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Budget deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Budget Found");
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







}
