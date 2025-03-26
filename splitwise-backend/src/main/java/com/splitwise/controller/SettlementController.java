package com.splitwise.controller;

import com.splitwise.dto.SettlementRequest;
import com.splitwise.model.Settlement;
import com.splitwise.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
@CrossOrigin("*")
public class SettlementController {

    @Autowired
    private SettlementService service;

    @PostMapping("/settle")
    public String settleExpense(@RequestBody SettlementRequest request) {
        Settlement settlement = new Settlement();
        settlement.setPayerId(request.getPayerId());
        settlement.setPayeeId(request.getPayeeId());
        settlement.setAmount(request.getAmount());
        return service.settleUp(settlement);
    }

    @GetMapping("/user/{userId}")
    public List<Settlement> getUserSettlements(@PathVariable Long userId) {
        return service.getSettlementsForUser(userId);
    }
}
