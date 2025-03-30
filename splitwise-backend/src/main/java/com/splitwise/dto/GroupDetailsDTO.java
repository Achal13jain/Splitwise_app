package com.splitwise.dto;

import java.util.List;

public class GroupDetailsDTO {
    private Long groupId;
    private String name;
    private List<GroupMemberDTO> members;
    private List<ExpenseDTO> expenses;
    private List<String> balances;
    private List<SettlementDTO> settlements;
    private boolean isSettled;

    public GroupDetailsDTO() {}

    public GroupDetailsDTO(Long groupId, String name, List<GroupMemberDTO> members,
                           List<ExpenseDTO> expenses, List<String> balances) {
        this.groupId = groupId;
        this.name = name;
        this.members = members;
        this.expenses = expenses;
        this.balances = balances;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberDTO> members) {
        this.members = members;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }

    public List<String> getBalances() {
        return balances;
    }

    public void setBalances(List<String> balances) {
        this.balances = balances;
    }

    public List<SettlementDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<SettlementDTO> settlements) {
        this.settlements = settlements;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setIsSettled(boolean isSettled) {
        this.isSettled = isSettled;
    }
}
