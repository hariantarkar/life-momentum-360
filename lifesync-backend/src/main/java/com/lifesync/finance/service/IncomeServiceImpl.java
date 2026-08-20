package com.lifesync.finance.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.finance.dto.IncomeRequest;
import com.lifesync.finance.dto.IncomeResponse;
import com.lifesync.finance.entity.Income;
import com.lifesync.finance.repository.IncomeRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public IncomeResponse create(Long userId, IncomeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Income income = new Income();
        applyRequest(income, request);
        income.setUser(user);

        Income saved = incomeRepository.save(income);
        return toResponse(saved);
    }

    @Override
    public List<IncomeResponse> getAll(Long userId) {
        return incomeRepository.findByUserIdOrderByIncomeDateDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public IncomeResponse getById(Long userId, Long incomeId) {
        return toResponse(getOwned(userId, incomeId));
    }

    @Override
    @Transactional
    public IncomeResponse update(Long userId, Long incomeId, IncomeRequest request) {
        Income income = getOwned(userId, incomeId);
        applyRequest(income, request);
        return toResponse(incomeRepository.save(income));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long incomeId) {
        incomeRepository.delete(getOwned(userId, incomeId));
    }

    private Income getOwned(Long userId, Long incomeId) {
        return incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));
    }

    private void applyRequest(Income income, IncomeRequest request) {
        income.setAmount(request.getAmount());
        income.setCategory(request.getCategory());
        income.setDescription(request.getDescription());
        income.setIncomeDate(request.getIncomeDate());
        income.setRecurring(request.isRecurring());
        income.setRecurrenceFrequency(request.getRecurrenceFrequency());
    }

    private IncomeResponse toResponse(Income income) {
        IncomeResponse dto = new IncomeResponse();
        dto.setId(income.getId());
        dto.setAmount(income.getAmount());
        dto.setCategory(income.getCategory());
        dto.setDescription(income.getDescription());
        dto.setIncomeDate(income.getIncomeDate());
        dto.setRecurring(income.isRecurring());
        dto.setRecurrenceFrequency(income.getRecurrenceFrequency());
        dto.setCreatedAt(income.getCreatedAt());
        dto.setUpdatedAt(income.getUpdatedAt());
        return dto;
    }
}