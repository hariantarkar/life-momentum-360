package com.lifesync.lifearea.service;

import com.lifesync.common.exception.BadRequestException;
import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.lifearea.dto.LifeAreaRequest;
import com.lifesync.lifearea.dto.LifeAreaResponse;
import com.lifesync.lifearea.entity.LifeArea;
import com.lifesync.lifearea.repository.LifeAreaRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LifeAreaServiceImpl implements LifeAreaService {

    @Autowired
    private LifeAreaRepository lifeAreaRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public LifeAreaResponse create(Long userId, LifeAreaRequest request) {

        if (lifeAreaRepository.existsByUserIdAndNameIgnoreCaseAndActiveTrue(userId, request.getName())) {
            throw new BadRequestException("A life area with this name already exists");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LifeArea area = new LifeArea();
        area.setName(request.getName());
        area.setDescription(request.getDescription());
        area.setColorCode(request.getColorCode());
        area.setIcon(request.getIcon());
        area.setUser(user);
        area.setActive(true);

        LifeArea saved = lifeAreaRepository.save(area);
        return LifeAreaResponse.from(saved);
    }

    @Override
    public List<LifeAreaResponse> getAll(Long userId) {
        return lifeAreaRepository.findByUserIdAndActiveTrueOrderByNameAsc(userId)
                .stream()
                .map(LifeAreaResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public LifeAreaResponse getById(Long userId, Long id) {
        LifeArea area = lifeAreaRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Life area not found"));
        return LifeAreaResponse.from(area);
    }

    @Override
    @Transactional
    public LifeAreaResponse update(Long userId, Long id, LifeAreaRequest request) {
        LifeArea area = lifeAreaRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Life area not found"));

        area.setName(request.getName());
        area.setDescription(request.getDescription());
        area.setColorCode(request.getColorCode());
        area.setIcon(request.getIcon());

        LifeArea saved = lifeAreaRepository.save(area);
        return LifeAreaResponse.from(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        LifeArea area = lifeAreaRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Life area not found"));

        // Soft delete — keeps history intact for goals/tasks that may reference this area later
        area.setActive(false);
        lifeAreaRepository.save(area);
    }
}