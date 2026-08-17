package com.lifesync.lifearea.repository;

import com.lifesync.lifearea.entity.LifeArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LifeAreaRepository extends JpaRepository<LifeArea, Long> {

    List<LifeArea> findByUserIdAndActiveTrueOrderByNameAsc(Long userId);

    Optional<LifeArea> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndNameIgnoreCaseAndActiveTrue(Long userId, String name);

    long countByUserIdAndActiveTrue(Long userId);
}