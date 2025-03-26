package com.splitwise.service;

import com.splitwise.dto.GroupRequest;
import com.splitwise.dto.GroupResponse;
import com.splitwise.dto.GroupWithMembersRequest;
import com.splitwise.model.Group;
import com.splitwise.model.GroupMember;
import com.splitwise.model.User;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.GroupRepository;
import com.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

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

    public List<GroupMember> getUserGroups(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? groupMemberRepository.findByUser(user) : null;
    }
//    public List<GroupResponse> getGroupsByUserEmail(String email) {
//        List<GroupMember> memberships = groupMemberRepository.findByEmail(email);
//
//        return memberships.stream()
//                .map(m -> new GroupResponse(m.getGroup().getId(), m.getGroup().getName()))
//                .collect(Collectors.toList());
//    }
public List<GroupResponse> getGroupsByUserEmail(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
        return List.of(); // or throw an exception if preferred
    }

    List<GroupMember> members = groupMemberRepository.findByUser(user);

    return members.stream()
            .map(m -> new GroupResponse(m.getGroup().getId(), m.getGroup().getName()))
            .toList();
}
//    public String createGroupWithMembers(GroupWithMembersRequest request) {
//        User creator = userRepository.findById(request.getUserId()).orElse(null);
//        if (creator == null) return "User not found!";
//
//        Group group = new Group();
//        group.setName(request.getName());
//        Group savedGroup = groupRepository.save(group);
//
//        // Add creator to group
//        GroupMember gm = new GroupMember();
//        gm.setGroup(savedGroup);
//        gm.setUser(creator);
//        groupMemberRepository.save(gm);
//
//        // Add additional members
//        for (String username : request.getMemberUsernames()) {
//            User member = userRepository.findByName(username);
//            if (member != null) {
//                GroupMember memberEntry = new GroupMember();
//                memberEntry.setGroup(savedGroup);
//                memberEntry.setUser(member);
//                groupMemberRepository.save(memberEntry);
//            }
//        }
//
//        return "Group created with members successfully!";
//    }
public String createGroupWithMembers(GroupWithMembersRequest request) {
    // Create group
    Group group = new Group();
    group.setName(request.getName());
    Group savedGroup = groupRepository.save(group);

    // Add the creator
    User creator = userRepository.findById(request.getUserId()).orElse(null);
    if (creator == null) return "❌ Creator not found.";
    groupMemberRepository.save(new GroupMember(savedGroup, creator));

    // Add other members by name
    for (String username : request.getMemberUsernames()) {
        User user = userRepository.findByName(username);
        if (user == null) return "❌ User not found: " + username;
        groupMemberRepository.save(new GroupMember(savedGroup, user));
    }

    return "✅ Group created with members!";
}

}
