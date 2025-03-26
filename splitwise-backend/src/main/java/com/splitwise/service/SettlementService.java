package com.splitwise.service;

import com.splitwise.model.Settlement;
import com.splitwise.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepo;

    public String settleUp(Settlement settlement) {
        settlementRepo.save(settlement);
        return "✅ Expense settled successfully!";
    }

    public List<Settlement> getSettlementsForUser(Long userId) {
        return settlementRepo.findByPayerIdOrPayeeId(userId, userId);
    }
}
