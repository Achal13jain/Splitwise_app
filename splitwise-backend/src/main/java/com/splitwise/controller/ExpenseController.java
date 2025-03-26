package com.splitwise.controller;

import com.splitwise.dto.ExpenseRequest;
import com.splitwise.model.Expense;
import com.splitwise.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public String addExpense(@RequestBody ExpenseRequest request) {
        return expenseService.addExpense(request);
    }

    @GetMapping("/group/{groupId}")
    public List<Expense> getExpensesByGroup(@PathVariable("groupId") Long groupId) {
        return expenseService.getExpensesByGroup(groupId);
    }
}
