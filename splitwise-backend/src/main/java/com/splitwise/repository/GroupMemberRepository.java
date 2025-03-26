package com.splitwise.repository;

import com.splitwise.model.Group;
import com.splitwise.model.GroupMember;
import com.splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByUser(User user);
    List<GroupMember> findByGroupId(Long groupId);
}
