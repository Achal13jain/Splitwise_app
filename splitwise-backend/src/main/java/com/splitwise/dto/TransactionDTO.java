
package com.splitwise.dto;

import java.time.LocalDateTime;

public class TransactionDTO {
    private String description;
    private Double amount;
    private String groupName;
    private String paidBy;
    private String type; // EXPENSE or SETTLEMENT
    private LocalDateTime date;

    public TransactionDTO() {}

    public TransactionDTO(String description, Double amount, String groupName, String paidBy, String type, LocalDateTime date) {
        this.description = description;
        this.amount = amount;
        this.groupName = groupName;
        this.paidBy = paidBy;
        this.type = type;
        this.date = date;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
