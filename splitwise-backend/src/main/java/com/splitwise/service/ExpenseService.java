package com.splitwise.service;

import com.splitwise.dto.ExpenseRequest;
import com.splitwise.dto.UserBalanceDTO;
import com.splitwise.model.Expense;
import com.splitwise.model.Group;
import com.splitwise.model.User;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.GroupRepository;
import com.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.splitwise.model.GroupMember;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public String addExpense(ExpenseRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).orElse(null);
        User payer = userRepository.findById(request.getPayerId()).orElse(null);

        if (group == null || payer == null) {
            return "Invalid group or payer ID";
        }

        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setGroup(group);
        expense.setPayer(payer);
        expense.setDate(LocalDate.now());

        expenseRepository.save(expense);
        return "Expense added successfully!";
    }

    public List<Expense> getExpensesByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        return group != null ? expenseRepository.findByGroup(group) : null;
    }
    public List<UserBalanceDTO> calculateGroupBalances(Long groupId) {
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);

        Map<Long, Double> paidMap = new HashMap<>();
        Map<Long, Double> owedMap = new HashMap<>();

        for (GroupMember gm : members) {
            paidMap.put(gm.getUser().getId(), 0.0);
            owedMap.put(gm.getUser().getId(), 0.0);
        }

        for (Expense e : expenses) {
            Long payerId = e.getPayer().getId();
            double amount = e.getAmount();
            double split = amount / members.size();

            // Add full to payer
            paidMap.put(payerId, paidMap.get(payerId) + amount);

            // Add split share to each as owed
            for (GroupMember gm : members) {
                owedMap.put(gm.getUser().getId(), owedMap.get(gm.getUser().getId()) + split);
            }
        }

        List<UserBalanceDTO> balances = new ArrayList<>();
        for (GroupMember gm : members) {
            Long uid = gm.getUser().getId();
            double balance = paidMap.get(uid) - owedMap.get(uid);
            balances.add(new UserBalanceDTO(gm.getUser().getName(), gm.getUser().getEmail(), balance));
        }

        return balances;
    }

}
