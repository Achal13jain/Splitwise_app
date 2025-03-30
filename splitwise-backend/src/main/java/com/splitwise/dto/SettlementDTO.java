package com.splitwise.dto;

import java.time.LocalDate;

public class SettlementDTO {
    private Long payerId;
    private String payerName;
    private Long payeeId;
    private String payeeName;
    private double amount;
    private LocalDate date;

    public SettlementDTO(Long payerId, String payerName, Long payeeId, String payeeName, double amount, LocalDate date) {
        this.payerId = payerId;
        this.payerName = payerName;
        this.payeeId = payeeId;
        this.payeeName = payeeName;
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public Long getPayerId() { return payerId; }
    public void setPayerId(Long payerId) { this.payerId = payerId; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public Long getPayeeId() { return payeeId; }
    public void setPayeeId(Long payeeId) { this.payeeId = payeeId; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
