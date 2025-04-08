package com.splitwise.service;

import com.splitwise.dto.ExpenseRequest;
import com.splitwise.dto.SettlementRequest;
import com.splitwise.dto.TransactionDTO;
import com.splitwise.model.*;
import com.splitwise.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private SettlementRepository settlementRepository;

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

    public List<String> calculateGroupBalances(Long groupId, Long loggedInUserId) {
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) return List.of("Group not found!");
        List<Settlement> settlements = settlementRepository.findByGroup(group);

        Map<Long, Double> paidMap = new HashMap<>();
        Map<Long, Double> owedMap = new HashMap<>();

        for (GroupMember gm : members) {
            Long uid = gm.getUser().getId();
            paidMap.put(uid, 0.0);
            owedMap.put(uid, 0.0);
        }

        //  Distribute expenses equally
        for (Expense e : expenses) {
            Long payerId = e.getPayer().getId();
            double amount = e.getAmount();
            double split = amount / members.size();

            paidMap.put(payerId, paidMap.get(payerId) + amount);
            for (GroupMember gm : members) {
                Long uid = gm.getUser().getId();
                owedMap.put(uid, owedMap.get(uid) + split);
            }
        }

        // Compute net balances from expenses
        Map<Long, Double> netBalances = new HashMap<>();
        for (Long uid : paidMap.keySet()) {
            double net = paidMap.get(uid) - owedMap.get(uid);
            netBalances.put(uid, net);
        }

        //  Apply settlements to net balances
        for (Settlement s : settlements) {
            Long payerId = s.getPayerId();
            Long payeeId = s.getPayeeId();
            double amount = s.getAmount();

            // Payer's debt decreases (balance increases)
            netBalances.put(payerId, netBalances.get(payerId) + amount);
            // Payee's credit decreases (balance decreases)
            netBalances.put(payeeId, netBalances.get(payeeId) - amount);
        }

        // Generate messages for the logged-in user
        List<String> messages = new ArrayList<>();
        double myBalance = netBalances.get(loggedInUserId);

        // If overall balance is settled, report it immediately.
        if (Math.abs(myBalance) < 0.01) {
            messages.add("You are all settled up!");
            return messages;
        }

        // If logged in user has a positive balance (i.e., others owe money)
        if (myBalance > 0) {
            // For each other user with a negative net balance, they owe you exactly the absolute value.
            for (GroupMember gm : members) {
                Long otherId = gm.getUser().getId();
                if (otherId.equals(loggedInUserId)) continue;
                double otherBalance = netBalances.get(otherId);
                if (otherBalance < 0) {
                    double amount = -otherBalance; // because it's negative
                    messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", amount));
                }
            }
        } else { // Logged in user has a negative balance (i.e., they owe money)
            // Sum of positive net balances among others
            double totalPositive = 0;
            for (GroupMember gm : members) {
                Long otherId = gm.getUser().getId();
                if (otherId.equals(loggedInUserId)) continue;
                double otherBalance = netBalances.get(otherId);
                if (otherBalance > 0) {
                    totalPositive += otherBalance;
                }
            }
            // Distribute the debt proportionally among positive net users
            for (GroupMember gm : members) {
                Long otherId = gm.getUser().getId();
                if (otherId.equals(loggedInUserId)) continue;
                double otherBalance = netBalances.get(otherId);
                if (otherBalance > 0 && totalPositive > 0) {
                    double amount = (-myBalance) * (otherBalance / totalPositive);
                    messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", amount));
                }
            }
        }

        if (messages.isEmpty()) {
            messages.add("You are all settled up!");
        }
        return messages;
    }


    // Added settleUp method
    public String settleUp(SettlementRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).orElse(null);
        if (group == null) return "Group not found!";

        Long payerId = request.getPayerId();
        Long payeeId = request.getPayeeId();

        String payeeName = userRepository.findById(payeeId).map(User::getName).orElse("Unknown");

        List<String> balances = calculateGroupBalances(request.getGroupId(), payerId);

        double amountToSettle = 0.0;
        for (String msg : balances) {
            if (msg.contains("You owe") && msg.contains(payeeName)) {
                String[] parts = msg.split("₹");
                if (parts.length == 2) {
                    try {
                        amountToSettle = Double.parseDouble(parts[1].trim());
                    } catch (NumberFormatException e) {
                        return "Error parsing settlement amount.";
                    }
                }
                break;
            }
        }

        if (amountToSettle <= 0.0) {
            return "No dues to settle with " + payeeName;
        }

        Settlement settlement = new Settlement();
        settlement.setGroup(group);
        settlement.setPayerId(payerId);
        settlement.setPayeeId(payeeId);
        settlement.setAmount(amountToSettle);
        settlement.setDate(LocalDate.now());

        settlementRepository.save(settlement);
        return "Successfully settled ₹" + String.format("%.2f", amountToSettle) + " with " + payeeName;
    }
    public List<TransactionDTO> getPersonalTransactions(Long userId) {
        List<TransactionDTO> transactions = new ArrayList<>();

        List<Expense> expenses = expenseRepository.findByPayerId(userId);
        for (Expense e : expenses) {
            TransactionDTO dto = new TransactionDTO(
                    e.getDescription(),
                    e.getAmount(),
                    e.getGroup().getName(),
                    e.getPayer().getName(), // Use getName()
                    "EXPENSE",
                    e.getDate().atStartOfDay() // Use getDate()
            );
            transactions.add(dto);
        }

        List<Settlement> settlements = settlementRepository.findByPayerIdOrPayeeId(userId, userId);
        for (Settlement s : settlements) {
            User payer = userRepository.findById(s.getPayerId()).orElse(null);
            User payee = userRepository.findById(s.getPayeeId()).orElse(null);

            String description = "Settlement between " +
                    (payer != null ? payer.getName() : "Unknown") +
                    " and " +
                    (payee != null ? payee.getName() : "Unknown");

            TransactionDTO dto = new TransactionDTO(
                    description,
                    s.getAmount(),
                    s.getGroup().getName(),
                    (payer != null ? payer.getName() : "Unknown"),
                    "SETTLEMENT",
                    s.getDate().atStartOfDay() // Use getDate()
            );
            transactions.add(dto);
        }

        transactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return transactions;
    }
}

