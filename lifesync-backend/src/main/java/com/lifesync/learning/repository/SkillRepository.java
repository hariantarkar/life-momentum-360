package com.lifesync.learning.repository;

import com.lifesync.learning.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByUserIdOrderByNameAsc(Long userId);

    Optional<Skill> findByIdAndUserId(Long id, Long userId);
}