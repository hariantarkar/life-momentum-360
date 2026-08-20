package com.lifesync.finance.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.finance.dto.ExpenseRequest;
import com.lifesync.finance.dto.ExpenseResponse;
import com.lifesync.finance.entity.Expense;
import com.lifesync.finance.repository.ExpenseRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ExpenseResponse create(Long userId, ExpenseRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = new Expense();
        applyRequest(expense, request);
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    @Override
    public List<ExpenseResponse> getAll(Long userId) {
        return expenseRepository.findByUserIdOrderByExpenseDateDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse getById(Long userId, Long expenseId) {
        return toResponse(getOwned(userId, expenseId));
    }

    @Override
    @Transactional
    public ExpenseResponse update(Long userId, Long expenseId, ExpenseRequest request) {
        Expense expense = getOwned(userId, expenseId);
        applyRequest(expense, request);
        return toResponse(expenseRepository.save(expense));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long expenseId) {
        expenseRepository.delete(getOwned(userId, expenseId));
    }

    private Expense getOwned(Long userId, Long expenseId) {
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    private void applyRequest(Expense expense, ExpenseRequest request) {
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setRecurring(request.isRecurring());
        expense.setRecurrenceFrequency(request.getRecurrenceFrequency());
    }

    private ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse dto = new ExpenseResponse();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setDescription(expense.getDescription());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setRecurring(expense.isRecurring());
        dto.setRecurrenceFrequency(expense.getRecurrenceFrequency());
        dto.setCreatedAt(expense.getCreatedAt());
        dto.setUpdatedAt(expense.getUpdatedAt());
        return dto;
    }
}