package com.splitwise.dto;

import java.util.List;

public class GroupDetailsDTO {
    private Long id;
    private String name;
    private List<GroupMemberDTO> members;
    private List<ExpenseDTO> expenses;
    private List<UserBalanceDTO> balances;

    public GroupDetailsDTO(Long id, String name, List<GroupMemberDTO> members, List<ExpenseDTO> expenses, List<UserBalanceDTO> balances) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.expenses = expenses;
        this.balances = balances;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<GroupMemberDTO> getMembers() { return members; }
    public void setMembers(List<GroupMemberDTO> members) { this.members = members; }

    public List<ExpenseDTO> getExpenses() { return expenses; }
    public void setExpenses(List<ExpenseDTO> expenses) { this.expenses = expenses; }

    public List<UserBalanceDTO> getBalances() { return balances; }
    public void setBalances(List<UserBalanceDTO> balances) { this.balances = balances; }
}
