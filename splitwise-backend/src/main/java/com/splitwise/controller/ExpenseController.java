//package com.splitwise.controller;
//
//import com.splitwise.dto.ExpenseRequest;
//import com.splitwise.dto.UserBalanceDTO;
//import com.splitwise.model.Expense;
//import com.splitwise.service.ExpenseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/expenses")
//@CrossOrigin(origins = "*")
//public class ExpenseController {
//
//    @Autowired
//    private ExpenseService expenseService;
//
//    @PostMapping("/add")
//    public String addExpense(@RequestBody ExpenseRequest request) {
//        return expenseService.addExpense(request);
//    }
//
//    @GetMapping("/group/{groupId}")
//    public List<Expense> getExpensesByGroup(@PathVariable("groupId") Long groupId) {
//        return expenseService.getExpensesByGroup(groupId);
//    }
//
//    @GetMapping("/group/{groupId}/balances")
//    public List<String> getGroupBalances(@PathVariable Long groupId, @RequestParam("userId") Long userId) {
//        return expenseService.calculateGroupBalances(groupId, userId);
//    }
//
//}
//package com.splitwise.controller;
//
//import com.splitwise.dto.ExpenseRequest;
//import com.splitwise.dto.UserBalanceDTO;
//import com.splitwise.model.Expense;
//import com.splitwise.service.ExpenseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//import com.splitwise.dto.SettlementRequest;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/expenses")
//@CrossOrigin(origins = "*")
//public class ExpenseController {
//
//    @Autowired
//    private ExpenseService expenseService;
//
//    @PostMapping("/add")
//    public String addExpense(@RequestBody ExpenseRequest request) {
//        return expenseService.addExpense(request);
//    }
//
//    @GetMapping("/group/{groupId}")
//    public List<Expense> getExpensesByGroup(@PathVariable("groupId") Long groupId) {
//        return expenseService.getExpensesByGroup(groupId);
//    }
//
//    @GetMapping("/group/{groupId}/balances")
//    public List<String> getGroupBalances(@PathVariable Long groupId, @RequestParam("userId") Long userId) {
//        return expenseService.calculateGroupBalances(groupId, userId);
//    }
//
//
//    @PostMapping("/settle")
//    public ResponseEntity<String> settleUp(@RequestBody SettlementRequest request) {
//        String response = expenseService.settleUp(request);
//        return ResponseEntity.ok(response);
//    }
//
//}


package com.splitwise.controller;

import com.splitwise.dto.SettlementRequest;
import com.splitwise.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/settle")
    public ResponseEntity<String> settleUp(@RequestBody SettlementRequest request) {
        String response = expenseService.settleUp(request);
        return ResponseEntity.ok(response);
    }
}
