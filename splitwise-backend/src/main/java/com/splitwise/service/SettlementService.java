package com.splitwise.service;

import com.splitwise.model.Group;
import com.splitwise.model.Settlement;
import com.splitwise.repository.GroupRepository;
import com.splitwise.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepo;

    @Autowired
    private GroupRepository groupRepository;

    public String settleUp(Settlement settlement) {
        // Convert groupId to Group object
        Group group = groupRepository.findById(settlement.getGroup().getId()).orElse(null);
        if (group == null) {
            return "❌ Group not found!";
        }

        settlement.setGroup(group);
        settlementRepo.save(settlement);
        return "✅ Expense settled successfully!";
    }

    public List<Settlement> getSettlementsForUser(Long userId) {
        return settlementRepo.findByPayerIdOrPayeeId(userId, userId);
    }
}
