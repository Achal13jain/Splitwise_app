//update 2 - 30-03

package com.splitwise.service;

import com.splitwise.dto.*;
import com.splitwise.model.*;
import com.splitwise.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private SettlementRepository settlementRepository;


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

    public String createGroupWithMembers(GroupWithMembersRequest request) {
        Group group = new Group();
        group.setName(request.getName());
        Group savedGroup = groupRepository.save(group);

        User creator = userRepository.findById(request.getUserId()).orElse(null);
        if (creator == null) return "❌ Creator not found.";
        groupMemberRepository.save(new GroupMember(savedGroup, creator));

        for (String username : request.getMemberUsernames()) {
            User user = userRepository.findByName(username);
            if (user == null) return "❌ User not found: " + username;
            groupMemberRepository.save(new GroupMember(savedGroup, user));
        }

        return "✅ Group created with members!";
    }

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

    public GroupDetailsDTO getGroupDetails(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) return null;

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);
        List<GroupMemberDTO> members = groupMembers.stream()
                .map(gm -> new GroupMemberDTO(
                        gm.getUser().getId(),
                        gm.getUser().getName(),
                        gm.getUser().getEmail()
                ))
                .toList();

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

        List<String> balances = expenseService.calculateGroupBalances(groupId, userId);

        return new GroupDetailsDTO(group.getId(), group.getName(), members, expenses, balances);
    }

    public List<GroupMember> getUserGroups(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? groupMemberRepository.findByUser(user) : null;
    }


}
