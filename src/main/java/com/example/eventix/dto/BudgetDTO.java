package com.example.eventix.dto;

import com.example.eventix.entity.Budget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {

    private int budget_id;
    private String budget_name;
    private long budget_amount;
    private Budget.budgetType budget_type;
    private int event_id;
    private LocalDateTime created_at;

}
