package com.splitwise.controller;

import com.splitwise.dto.*;
//import com.splitwise.dto.GroupResponse;
import com.splitwise.model.GroupMember;
import com.splitwise.service.ExpenseService;
import com.splitwise.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private GroupService groupService;

//    @PostMapping("/create")
//    public String createGroup(@RequestBody GroupRequest request) {
//        return groupService.createGroup(request);
//    }
@PostMapping("/create")
public String createGroup(@RequestBody GroupWithMembersRequest request) {
    return groupService.createGroupWithMembers(request);
}

    @GetMapping("/user/{userId}")
    public List<GroupMember> getGroups(@PathVariable("userId") Long userId) {
        return groupService.getUserGroups(userId);
    }
//    @GetMapping("/user/email/{email}")
//    public List<GroupResponse> getGroupsByEmail(@PathVariable String email) {
//        return groupService.getGroupsByUserEmail(email);
//    }
@GetMapping("/user/email/{email}")
public List<GroupResponse> getGroupsByEmail(@PathVariable("email") String email) {
    return groupService.getGroupsByUserEmail(email);
}
    @GetMapping("/{groupId}/balances")
    public List<UserBalanceDTO> getGroupBalances(@PathVariable Long groupId) {
        return expenseService.calculateGroupBalances(groupId);
    }
    @PostMapping("/create-with-members")
    public String createGroupWithMembers(@RequestBody GroupWithMembersRequest request) {
        return groupService.createGroupWithMembers(request);
    }
    @GetMapping("/{id}/details")
    public GroupDetailsDTO getGroupDetails(@PathVariable("id") Long id) {
        return groupService.getGroupDetails(id);
    }

}
