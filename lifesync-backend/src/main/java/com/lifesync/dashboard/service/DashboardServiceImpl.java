package com.lifesync.dashboard.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.dashboard.dto.DashboardResponse;
import com.lifesync.lifearea.dto.LifeAreaResponse;
import com.lifesync.lifearea.repository.LifeAreaRepository;
import com.lifesync.user.entity.User;
import com.lifesync.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LifeAreaRepository lifeAreaRepository;

    @Override
    public DashboardResponse getDashboard(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<LifeAreaResponse> lifeAreas = lifeAreaRepository
                .findByUserIdAndActiveTrueOrderByNameAsc(userId)
                .stream()
                .map(LifeAreaResponse::from)
                .collect(Collectors.toList());

        DashboardResponse dashboard = new DashboardResponse();
        dashboard.setFullName(user.getFullName());
        dashboard.setEmail(user.getEmail());
        dashboard.setTotalLifeAreas(lifeAreas.size());
        dashboard.setLifeAreas(lifeAreas);

        return dashboard;
    }
}