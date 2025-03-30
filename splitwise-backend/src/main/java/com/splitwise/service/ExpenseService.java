//update 2

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
//
//        // ✅ Fetch the Group object first
//        Group group = groupRepository.findById(groupId).orElse(null);
//        if (group == null) return List.of("Group not found!");
//
//        // ✅ Now fetch settlements using Group
//        List<Settlement> settlements = settlementRepository.findByGroup(group);
//
//        Map<Long, Double> paidMap = new HashMap<>();
//        Map<Long, Double> owedMap = new HashMap<>();
//
//        for (GroupMember gm : members) {
//            paidMap.put(gm.getUser().getId(), 0.0);
//            owedMap.put(gm.getUser().getId(), 0.0);
//        }
//
//        // 1️⃣ Distribute expense data
//        for (Expense e : expenses) {
//            Long payerId = e.getPayer().getId();
//            double amount = e.getAmount();
//            double split = amount / members.size();
//
//            paidMap.put(payerId, paidMap.get(payerId) + amount);
//            for (GroupMember gm : members) {
//                owedMap.put(gm.getUser().getId(), owedMap.get(gm.getUser().getId()) + split);
//            }
//        }
//
//        // 2️⃣ Initial net balances
//        Map<Long, Double> netBalances = new HashMap<>();
//        for (Long userId : paidMap.keySet()) {
//            double net = paidMap.get(userId) - owedMap.get(userId);
//            netBalances.put(userId, net);
//        }
//
//        // 3️⃣ Apply settlements ➖ Subtract only from payer
//        for (Settlement s : settlements) {
//            Long payer = s.getPayerId();
//            if (netBalances.containsKey(payer)) {
//                netBalances.put(payer, netBalances.get(payer) - s.getAmount());
//            }
//        }
//
//        // 4️⃣ Format balances
//        List<String> messages = new ArrayList<>();
//        double userBalance = netBalances.get(loggedInUserId);
//
//        for (GroupMember gm : members) {
//            Long otherId = gm.getUser().getId();
//            if (otherId.equals(loggedInUserId)) continue;
//
//            double otherBalance = netBalances.get(otherId);
//            double balanceDiff = otherBalance - userBalance;
//
//            if (balanceDiff > 0.01) {
//                messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", balanceDiff));
//            } else if (balanceDiff < -0.01) {
//                messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", Math.abs(balanceDiff)));
//            }
//
//        }
//
//        if (messages.isEmpty()) {
//            messages.add("You are all settled up!");
//        }
//
//        return messages;
//    }
//}

//30-03
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
//        Group group = groupRepository.findById(groupId).orElse(null);
//        if (group == null) return List.of("Group not found!");
//        List<Settlement> settlements = settlementRepository.findByGroup(group);
//
//        Map<Long, Double> paidMap = new HashMap<>();
//        Map<Long, Double> owedMap = new HashMap<>();
//
//        for (GroupMember gm : members) {
//            Long uid = gm.getUser().getId();
//            paidMap.put(uid, 0.0);
//            owedMap.put(uid, 0.0);
//        }
//
//        // Step 1: Track each member's share of expenses and payments
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
//        // Step 2: Apply settlements (subtract from payer, add to payee)
//        for (Settlement s : settlements) {
//            Long payerId = s.getPayerId();
//            Long payeeId = s.getPayeeId();
//            double amount = s.getAmount();
//
//            if (paidMap.containsKey(payerId)) {
//                paidMap.put(payerId, paidMap.get(payerId) - amount);
//            }
//            if (paidMap.containsKey(payeeId)) {
//                paidMap.put(payeeId, paidMap.get(payeeId) + amount);
//            }
//        }
//
//        // Step 3: Final net balance = paid - owed
//        Map<Long, Double> netBalances = new HashMap<>();
//        for (Long uid : paidMap.keySet()) {
//            double net = paidMap.get(uid) - owedMap.get(uid);
//            netBalances.put(uid, net);
//        }
//
//        List<String> messages = new ArrayList<>();
//        double myBalance = netBalances.get(loggedInUserId);
//
//        for (GroupMember gm : members) {
//            Long otherId = gm.getUser().getId();
//            if (otherId.equals(loggedInUserId)) continue;
//
//            double otherBalance = netBalances.get(otherId);
//            double amount = (otherBalance - myBalance)/ members.size();
//
//            if (amount > 0.01) {
//                messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", amount));
//            } else if (amount < -0.01) {
//                messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", Math.abs(amount)));
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



//30-03-25
//package com.splitwise.service;
//
//import com.splitwise.dto.ExpenseRequest;
//import com.splitwise.dto.SettlementRequest;
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
//        Group group = groupRepository.findById(groupId).orElse(null);
//        if (group == null) return List.of("Group not found!");
//        List<Settlement> settlements = settlementRepository.findByGroup(group);
//
//        Map<Long, Double> paidMap = new HashMap<>();
//        Map<Long, Double> owedMap = new HashMap<>();
//
//        for (GroupMember gm : members) {
//            Long uid = gm.getUser().getId();
//            paidMap.put(uid, 0.0);
//            owedMap.put(uid, 0.0);
//        }
//
//        // Step 1: Track each member's share of expenses and payments
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
//        // Step 2: Apply settlements (subtract from payer, add to payee)
//        for (Settlement s : settlements) {
//            Long payerId = s.getPayerId();
//            Long payeeId = s.getPayeeId();
//            double amount = s.getAmount();
//
//            if (paidMap.containsKey(payerId)) {
//                paidMap.put(payerId, paidMap.get(payerId) - amount);
//            }
//            if (paidMap.containsKey(payeeId)) {
//                paidMap.put(payeeId, paidMap.get(payeeId) + amount);
//            }
//        }
//
//        // Step 3: Final net balance = paid - owed
//        Map<Long, Double> netBalances = new HashMap<>();
//        for (Long uid : paidMap.keySet()) {
//            double net = paidMap.get(uid) - owedMap.get(uid);
//            netBalances.put(uid, net);
//        }
//
//        List<String> messages = new ArrayList<>();
//        double myBalance = netBalances.get(loggedInUserId);
//
//        for (GroupMember gm : members) {
//            Long otherId = gm.getUser().getId();
//            if (otherId.equals(loggedInUserId)) continue;
//
//            double otherBalance = netBalances.get(otherId);
//            double amount = (otherBalance - myBalance)/ members.size();
//
//            if (amount > 0.01) {
//                messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", amount));
//            } else if (amount < -0.01) {
//                messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", Math.abs(amount)));
//            }
//        }
//
//        if (messages.isEmpty()) {
//            messages.add("You are all settled up!");
//        }
//
//        return messages;
//    }
//
//    public String settleUp(SettlementRequest request) {
//        Group group = groupRepository.findById(request.getGroupId()).orElse(null);
//        if (group == null) return "Group not found!";
//
//        Long payerId = request.getPayerId();
//        Long payeeId = request.getPayeeId();
//
//        // Get the payee name
//        String payeeName = userRepository.findById(payeeId).map(User::getName).orElse("Unknown");
//
//        // Get balances from the perspective of the payer
//        List<String> balances = calculateGroupBalances(request.getGroupId(), payerId);
//
//        double amountToSettle = 0.0;
//        for (String msg : balances) {
//            if (msg.contains("You owe") && msg.contains(payeeName)) {
//                String[] parts = msg.split("₹");
//                if (parts.length == 2) {
//                    amountToSettle = Double.parseDouble(parts[1].trim());
//                }
//                break;
//            }
//        }
//
//        if (amountToSettle <= 0.0) {
//            return "No dues to settle with " + payeeName;
//        }
//
//        Settlement settlement = new Settlement();
//        settlement.setGroup(group);
//        settlement.setPayerId(payerId);
//        settlement.setPayeeId(payeeId);
//        settlement.setAmount(amountToSettle);
//        settlement.setDate(LocalDate.now());
//
//        settlementRepository.save(settlement);
//        return "Successfully settled ₹" + String.format("%.2f", amountToSettle) + " with " + payeeName;
//    }
//
//}


//update 3



package com.splitwise.service;

import com.splitwise.dto.ExpenseRequest;
import com.splitwise.dto.SettlementRequest;
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

        // Step 1: Track each member's share of expenses and payments
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

        // Step 2: Final net balance = paid - owed
        Map<Long, Double> netBalances = new HashMap<>();
        for (Long uid : paidMap.keySet()) {
            double net = paidMap.get(uid) - owedMap.get(uid);
            netBalances.put(uid, net);
        }

        // Step 3: Apply settlements to net balances
        for (Settlement s : settlements) {
            Long payerId = s.getPayerId();
            Long payeeId = s.getPayeeId();
            double amount = s.getAmount();

            netBalances.put(payerId, netBalances.get(payerId) - amount);
            netBalances.put(payeeId, netBalances.get(payeeId) + amount);
        }

        // Step 4: Create human-readable messages
        List<String> messages = new ArrayList<>();
        double myBalance = netBalances.get(loggedInUserId);

        for (GroupMember gm : members) {
            Long otherId = gm.getUser().getId();
            if (otherId.equals(loggedInUserId)) continue;

            double otherBalance = netBalances.get(otherId);
            double amount = (otherBalance - myBalance) / members.size();

            if (amount > 0.01) {
                messages.add("You owe " + gm.getUser().getName() + " ₹" + String.format("%.2f", amount));
            } else if (amount < -0.01) {
                messages.add(gm.getUser().getName() + " owes you ₹" + String.format("%.2f", Math.abs(amount)));
            }
        }

        if (messages.isEmpty()) {
            messages.add("You are all settled up!");
        }

        return messages;
    }

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
                    amountToSettle = Double.parseDouble(parts[1].trim());
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
}
