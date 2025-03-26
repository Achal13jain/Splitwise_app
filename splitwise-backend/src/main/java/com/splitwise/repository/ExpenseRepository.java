package com.splitwise.repository;

import com.splitwise.model.Expense;
import com.splitwise.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);
    List<Expense> findByGroupId(Long groupId);
}
