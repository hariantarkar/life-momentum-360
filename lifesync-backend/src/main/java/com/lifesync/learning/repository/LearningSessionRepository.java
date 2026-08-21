package com.lifesync.learning.repository;

import com.lifesync.learning.entity.LearningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {

    List<LearningSession> findByLearningPathIdOrderBySessionDateDesc(Long learningPathId);

    Optional<LearningSession> findByIdAndLearningPathId(Long id, Long learningPathId);

    @Query("SELECT COALESCE(SUM(s.durationMinutes), 0) FROM LearningSession s WHERE s.learningPath.id = :pathId")
    int sumDurationByLearningPathId(@Param("pathId") Long pathId);

    long countByLearningPathId(Long learningPathId);

    void deleteByLearningPathId(Long learningPathId);
}