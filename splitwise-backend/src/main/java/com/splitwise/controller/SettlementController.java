//package com.splitwise.controller;
//
//import com.splitwise.dto.SettlementRequest;
//import com.splitwise.model.Settlement;
//import com.splitwise.service.SettlementService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/settlements")
//@CrossOrigin("*")
//public class SettlementController {
//
//    @Autowired
//    private SettlementService service;
//
//    @PostMapping("/settle")
//    public String settleExpense(@RequestBody SettlementRequest request) {
//        Settlement settlement = new Settlement();
//        settlement.setGroupId(request.getGroupId());
//        settlement.setPayerId(request.getPayerId());
//        settlement.setPayeeId(request.getPayeeId());
//        settlement.setAmount(request.getAmount());
//        return service.settleUp(settlement);
//    }
//
//    @GetMapping("/user/{userId}")
//    public List<Settlement> getUserSettlements(@PathVariable Long userId) {
//        return service.getSettlementsForUser(userId);
//    }
//}


//30-03

package com.splitwise.controller;

import com.splitwise.dto.SettlementRequest;
import com.splitwise.model.Group;
import com.splitwise.model.Settlement;
import com.splitwise.repository.GroupRepository;
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

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping("/settle")
    public String settleExpense(@RequestBody SettlementRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).orElse(null);
        if (group == null) {
            return "❌ Invalid group ID!";
        }

        Settlement settlement = new Settlement();
        settlement.setGroup(group); // ✅ set Group object only
        settlement.setPayerId(request.getPayerId());
        settlement.setPayeeId(request.getPayeeId());

        return service.settleUp(settlement); // amount will be calculated inside
    }

    @GetMapping("/user/{userId}")
    public List<Settlement> getUserSettlements(@PathVariable Long userId) {
        return service.getSettlementsForUser(userId);
    }
}
