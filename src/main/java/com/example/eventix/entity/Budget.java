package com.example.eventix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "event_budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int budget_id;
    private String budget_name;
    private long budget_amount;

    public enum budgetType{
        INCOME,
        COST

    }

    @Enumerated(EnumType.STRING)
    private budgetType budget_type;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
