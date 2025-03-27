package com.splitwise.dto;

import java.time.LocalDate;

public class ExpenseDTO {
    private Long id;
    private String description;
    private Double amount;
    private String payerName;
    private LocalDate date;

    public ExpenseDTO(Long id, String description, Double amount, String payerName, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.payerName = payerName;
        this.date = date;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
