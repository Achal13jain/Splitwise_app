//package com.splitwise.service;
//
//import com.splitwise.dto.*;
//import com.splitwise.model.Group;
//import com.splitwise.model.GroupMember;
//import com.splitwise.model.User;
//import com.splitwise.model.Expense;
//import com.splitwise.repository.ExpenseRepository;
//import com.splitwise.repository.GroupMemberRepository;
//import com.splitwise.repository.GroupRepository;
//import com.splitwise.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class GroupService {
//
//    @Autowired
//    private ExpenseRepository expenseRepository;
//    @Autowired
//    private ExpenseService expenseService;
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    @Autowired
//    private GroupMemberRepository groupMemberRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public String createGroup(GroupRequest request) {
//        User user = userRepository.findById(request.getUserId()).orElse(null);
//        if (user == null) {
//            return "User not found!";
//        }
//
//        Group group = new Group();
//        group.setName(request.getName());
//
//        Group savedGroup = groupRepository.save(group);
//
//        GroupMember gm = new GroupMember();
//        gm.setGroup(savedGroup);
//        gm.setUser(user);
//        groupMemberRepository.save(gm);
//
//        return "Group created and user added!";
//    }
//
//    public List<GroupMember> getUserGroups(Long userId) {
//        User user = userRepository.findById(userId).orElse(null);
//        return user != null ? groupMemberRepository.findByUser(user) : null;
//    }
//
//public List<GroupResponse> getGroupsByUserEmail(String email) {
//    User user = userRepository.findByEmail(email);
//    if (user == null) {
//        return List.of(); // or throw an exception if preferred
//    }
//
//    List<GroupMember> members = groupMemberRepository.findByUser(user);
//
//    return members.stream()
//            .map(m -> new GroupResponse(m.getGroup().getId(), m.getGroup().getName()))
//            .toList();
//}
//
//public String createGroupWithMembers(GroupWithMembersRequest request) {
//    // Create group
//    Group group = new Group();
//    group.setName(request.getName());
//    Group savedGroup = groupRepository.save(group);
//
//    // Add the creator
//    User creator = userRepository.findById(request.getUserId()).orElse(null);
//    if (creator == null) return "❌ Creator not found.";
//    groupMemberRepository.save(new GroupMember(savedGroup, creator));
//
//    // Add other members by name
//    for (String username : request.getMemberUsernames()) {
//        User user = userRepository.findByName(username);
//        if (user == null) return "❌ User not found: " + username;
//        groupMemberRepository.save(new GroupMember(savedGroup, user));
//    }
//
//    return "✅ Group created with members!";
//}
//    public GroupDetailsDTO getGroupDetails(Long groupId) {
//        Group group = groupRepository.findById(groupId).orElse(null);
//        if (group == null) return null;
//
////        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
//        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);
//        List<GroupMemberDTO> members = groupMembers.stream()
//                .map(gm -> new GroupMemberDTO(
//                        gm.getUser().getId(),
//                        gm.getUser().getName(),
//                        gm.getUser().getEmail()
//                ))
//                .toList();
//
//        //List<Expense> expenses = expenseRepository.findByGroup(group);
//        List<Expense> rawExpenses = expenseRepository.findByGroup(group);
//        List<ExpenseDTO> expenses = rawExpenses.stream().map(e ->
//                new ExpenseDTO(
//                        e.getId(),
//                        e.getDescription(),
//                        e.getAmount(),
//                        e.getPayer().getName(),
//                        e.getDate()
//                )
//        ).toList();
//
//        List<UserBalanceDTO> balances = expenseService.calculateGroupBalances(groupId);
//
////        return new GroupDetailsDTO(group.getId(), group.getName(), members, expenses, balances);
//        return new GroupDetailsDTO(group.getId(), group.getName(), members, expenses, balances);
//
//    }
//
//}
package com.splitwise.service;

import com.splitwise.dto.*;
import com.splitwise.model.Group;
import com.splitwise.model.GroupMember;
import com.splitwise.model.User;
import com.splitwise.model.Expense;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.GroupRepository;
import com.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.splitwise.dto.GroupDetailsDTO;
import com.splitwise.dto.GroupMemberDTO;
import com.splitwise.dto.ExpenseDTO;
import com.splitwise.dto.UserBalanceDTO;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a group with only one user (creator)
    public String createGroup(GroupRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return "User not found!";
        }

        Group group = new Group();
        group.setName(request.getName());
        Group savedGroup = groupRepository.save(group);

        GroupMember gm = new GroupMember();
        gm.setGroup(savedGroup);
        gm.setUser(user);
        groupMemberRepository.save(gm);

        return "Group created and user added!";
    }

    // Create a group with multiple members
    public String createGroupWithMembers(GroupWithMembersRequest request) {
        Group group = new Group();
        group.setName(request.getName());
        Group savedGroup = groupRepository.save(group);

        // Add creator
        User creator = userRepository.findById(request.getUserId()).orElse(null);
        if (creator == null) return "❌ Creator not found.";
        groupMemberRepository.save(new GroupMember(savedGroup, creator));

        // Add other members
        for (String username : request.getMemberUsernames()) {
            User user = userRepository.findByName(username);
            if (user == null) return "❌ User not found: " + username;
            groupMemberRepository.save(new GroupMember(savedGroup, user));
        }

        return "✅ Group created with members!";
    }

    // Get all groups user is part of (by user email)
    public List<GroupResponse> getGroupsByUserEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return List.of();
        }

        List<GroupMember> members = groupMemberRepository.findByUser(user);
        return members.stream()
                .map(m -> new GroupResponse(m.getGroup().getId(), m.getGroup().getName()))
                .toList();
    }

    // Return full group details: members, expenses, balances
    public GroupDetailsDTO getGroupDetails(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) return null;

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);

        // Convert to DTOs
        List<GroupMemberDTO> members = groupMembers.stream()
                .map(gm -> new GroupMemberDTO(
                        gm.getUser().getId(),
                        gm.getUser().getName(),
                        gm.getUser().getEmail()
                )).toList();

        List<Expense> rawExpenses = expenseRepository.findByGroup(group);
        List<ExpenseDTO> expenses = rawExpenses.stream().map(e ->
                new ExpenseDTO(
                        e.getId(),
                        e.getDescription(),
                        e.getAmount(),
                        e.getPayer().getName(),
                        e.getDate()
                )
        ).toList();

        List<UserBalanceDTO> balances = expenseService.calculateGroupBalances(groupId);

        return new GroupDetailsDTO(group.getId(), group.getName(), members, expenses, balances);
    }

    // Optional: Get all group memberships by user ID (not used in frontend)
    public List<GroupMember> getUserGroups(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? groupMemberRepository.findByUser(user) : null;
    }
}
