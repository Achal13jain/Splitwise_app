//package com.splitwise.service;
//
//import com.splitwise.dto.ExpenseRequest;
//import com.splitwise.dto.UserBalanceDTO;
//import com.splitwise.model.*;
//import com.splitwise.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ExpenseService {
//
//    @Autowired
//    private SettlementRepository settlementRepository;
//
//    @Autowired
//    private GroupMemberRepository groupMemberRepository;
//
//    @Autowired
//    private ExpenseRepository expenseRepository;
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public String addExpense(ExpenseRequest request) {
//        Group group = groupRepository.findById(request.getGroupId()).orElse(null);
//        User payer = userRepository.findById(request.getPayerId()).orElse(null);
//
//        if (group == null || payer == null) {
//            return "Invalid group or payer ID";
//        }
//
//        Expense expense = new Expense();
//        expense.setAmount(request.getAmount());
//        expense.setDescription(request.getDescription());
//        expense.setGroup(group);
//        expense.setPayer(payer);
//        expense.setDate(LocalDate.now());
//
//        expenseRepository.save(expense);
//        return "Expense added successfully!";
//    }
//
//    public List<Expense> getExpensesByGroup(Long groupId) {
//        Group group = groupRepository.findById(groupId).orElse(null);
//        return group != null ? expenseRepository.findByGroup(group) : null;
//    }
//
//    public List<UserBalanceDTO> calculateGroupBalances(Long groupId) {
//        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
//        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
//        List<Settlement> settlements = settlementRepository.findAll(); // Apply only relevant settlements manually
//
//        Map<Long, Double> paidMap = new HashMap<>();
//        Map<Long, Double> owedMap = new HashMap<>();
//
//        // Initialize balances
//        for (GroupMember gm : members) {
//            Long uid = gm.getUser().getId();
//            paidMap.put(uid, 0.0);
//            owedMap.put(uid, 0.0);
//        }
//
//        // 1️⃣ Expense calculation
//        for (Expense e : expenses) {
//            Long payerId = e.getPayer().getId();
//            double amount = e.getAmount();
//            double split = amount / members.size();
//
//            paidMap.put(payerId, paidMap.get(payerId) + amount);
//
//            for (GroupMember gm : members) {
//                Long uid = gm.getUser().getId();
//                owedMap.put(uid, owedMap.get(uid) + split);
//            }
//        }
//
//        // 2️⃣ Initial balance calculation
//        Map<Long, Double> balanceMap = new HashMap<>();
//        for (Long userId : paidMap.keySet()) {
//            double balance = paidMap.get(userId) - owedMap.get(userId);
//            balanceMap.put(userId, balance);
//        }
//
//        // 3️⃣ Settlement application (payer pays → balance decreases, payee receives → balance increases)
//        for (Settlement s : settlements) {
//            Long payerId = s.getPayerId();
//            Long payeeId = s.getPayeeId();
//            double amount = s.getAmount();
//
//            if (balanceMap.containsKey(payerId)) {
//                balanceMap.put(payerId, balanceMap.get(payerId) - amount);
//            }
//            if (balanceMap.containsKey(payeeId)) {
//                balanceMap.put(payeeId, balanceMap.get(payeeId) - amount);
//            }
//        }
//
//        // 4️⃣ Convert to DTOs
//        List<UserBalanceDTO> balances = new ArrayList<>();
//        for (GroupMember gm : members) {
//            Long uid = gm.getUser().getId();
//            double finalBalance = balanceMap.get(uid);
//            balances.add(new UserBalanceDTO(gm.getUser().getName(), gm.getUser().getEmail(), finalBalance));
//        }
//
//        return balances;
//    }
//}
//update 1
//
//
//package com.splitwise.service;
//
//import com.splitwise.dto.ExpenseRequest;
//import com.splitwise.model.*;
//import com.splitwise.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.*;
//
//@Service
//public class ExpenseService {
//
//    @Autowired
//    private SettlementRepository settlementRepository;
//
//    @Autowired
//    private GroupMemberRepository groupMemberRepository;
//
//    @Autowired
//    private ExpenseRepository expenseRepository;
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public String addExpense(ExpenseRequest request) {
//        Group group = groupRepository.findById(request.getGroupId()).orElse(null);
//        User payer = userRepository.findById(request.getPayerId()).orElse(null);
//
//        if (group == null || payer == null) {
//            return "Invalid group or payer ID";
//        }
//
//        Expense expense = new Expense();
//        expense.setAmount(request.getAmount());
//        expense.setDescription(request.getDescription());
//        expense.setGroup(group);
//        expense.setPayer(payer);
//        expense.setDate(LocalDate.now());
//
//        expenseRepository.save(expense);
//        return "Expense added successfully!";
//    }
//
//    public List<Expense> getExpensesByGroup(Long groupId) {
//        Group group = groupRepository.findById(groupId).orElse(null);
//        return group != null ? expenseRepository.findByGroup(group) : null;
//    }
//
//    public List<String> calculateGroupBalances(Long groupId, Long loggedInUserId) {
//        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
//        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
//        List<Settlement> settlements = settlementRepository.findAll();
//
//        Map<Long, Double> paidMap = new HashMap<>();
//        Map<Long, Double> owedMap = new HashMap<>();
//
//        for (GroupMember gm : members) {
//            paidMap.put(gm.getUser().getId(), 0.0);
//            owedMap.put(gm.getUser().getId(), 0.0);
//        }
//
//        for (Expense e : expenses) {
//            Long payerId = e.getPayer().getId();
//            double amount = e.getAmount();
//            double split = amount / members.size();
//
//            paidMap.put(payerId, paidMap.get(payerId) + amount);
//
//            for (GroupMember gm : members) {
//                owedMap.put(gm.getUser().getId(), owedMap.get(gm.getUser().getId()) + split);
//            }
//        }
//
//        for (Settlement s : settlements) {
//            if (paidMap.containsKey(s.getPayerId())) {
//                paidMap.put(s.getPayerId(), paidMap.get(s.getPayerId()) + s.getAmount());
//            }
//            if (paidMap.containsKey(s.getPayeeId())) {
//                paidMap.put(s.getPayeeId(), paidMap.get(s.getPayeeId()) - s.getAmount());
//            }
//        }
//
//        Map<Long, Double> netBalances = new HashMap<>();
//        for (Long uid : paidMap.keySet()) {
//            netBalances.put(uid, paidMap.get(uid) - owedMap.get(uid));
//        }
//
//        List<String> messages = new ArrayList<>();
//        double userBalance = netBalances.get(loggedInUserId);
//
//        for (GroupMember gm : members) {
//            Long otherUserId = gm.getUser().getId();
//            if (otherUserId.equals(loggedInUserId)) continue;
//
//            double otherBalance = netBalances.get(otherUserId);
//            double difference = userBalance - otherBalance;
//            double split = difference / 2;
//
//            if (split > 0.01) {
//                messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", Math.abs(split)));
//            } else if (split < -0.01) {
//                messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", Math.abs(split)));
//            }
//        }
//
//        if (messages.isEmpty()) {
//            messages.add("You are all settled up!");
//        }
//
//        return messages;
//    }
//}

//update 2

package com.splitwise.service;

import com.splitwise.dto.ExpenseRequest;
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
        List<Settlement> settlements = settlementRepository.findAll(); // You could optimize this by filtering on group

        Map<Long, Double> paidMap = new HashMap<>();
        Map<Long, Double> owedMap = new HashMap<>();

        for (GroupMember gm : members) {
            paidMap.put(gm.getUser().getId(), 0.0);
            owedMap.put(gm.getUser().getId(), 0.0);
        }

        // 1️⃣ Distribute expense data
        for (Expense e : expenses) {
            Long payerId = e.getPayer().getId();
            double amount = e.getAmount();
            double split = amount / members.size();

            paidMap.put(payerId, paidMap.get(payerId) + amount);

            for (GroupMember gm : members) {
                owedMap.put(gm.getUser().getId(), owedMap.get(gm.getUser().getId()) + split);
            }
        }

        // 2️⃣ Initial net balances
        Map<Long, Double> netBalances = new HashMap<>();
        for (Long userId : paidMap.keySet()) {
            double net = paidMap.get(userId) - owedMap.get(userId);
            netBalances.put(userId, net);
        }

        // 3️⃣ Apply settlement correctly ➖ Subtract from payer only
        for (Settlement s : settlements) {
            Long payer = s.getPayerId();

            if (netBalances.containsKey(payer)) {
                netBalances.put(payer, netBalances.get(payer) - s.getAmount());
            }
            // ❌ Don't touch payee
        }

        // 4️⃣ Format balances for frontend
        List<String> messages = new ArrayList<>();
        double userBalance = netBalances.get(loggedInUserId);

        for (GroupMember gm : members) {
            Long otherId = gm.getUser().getId();
            if (otherId.equals(loggedInUserId)) continue;

            double otherBalance = netBalances.get(otherId);
            double diff = userBalance - otherBalance;
            double split = diff / 2;

            if (split > 0.01) {
                messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", split));
            } else if (split < -0.01) {
                messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", Math.abs(split)));
            }
        }

        if (messages.isEmpty()) {
            messages.add("You are all settled up!");
        }

        return messages;
    }
}
