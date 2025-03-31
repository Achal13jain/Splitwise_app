
package com.splitwise.controller;
import com.splitwise.dto.ExpenseRequest;
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

    @PostMapping("/expenses/add")
    public ResponseEntity<String> addExpense(@RequestBody ExpenseRequest request) {
        String response = expenseService.addExpense(request);
        return ResponseEntity.ok(response);
    }

}
