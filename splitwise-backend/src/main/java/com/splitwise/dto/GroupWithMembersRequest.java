package com.splitwise.dto;

import java.util.List;

public class GroupWithMembersRequest {
    private String name;
    private Long userId;
    private List<String> memberUsernames;

    // Getters & Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getMemberUsernames() {
        return memberUsernames;
    }
    public void setMemberUsernames(List<String> memberUsernames) {
        this.memberUsernames = memberUsernames;
    }
}
