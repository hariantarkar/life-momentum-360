package com.lifesync.learning.service;

import com.lifesync.common.exception.ResourceNotFoundException;
import com.lifesync.learning.dto.LearningSessionRequest;
import com.lifesync.learning.dto.LearningSessionResponse;
import com.lifesync.learning.entity.LearningPath;
import com.lifesync.learning.entity.LearningPathStatus;
import com.lifesync.learning.entity.LearningSession;
import com.lifesync.learning.repository.LearningPathRepository;
import com.lifesync.learning.repository.LearningSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LearningSessionServiceImpl implements LearningSessionService {

    @Autowired
    private LearningSessionRepository learningSessionRepository;

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Override
    @Transactional
    public LearningSessionResponse create(Long userId, Long pathId, LearningSessionRequest request) {
        LearningPath path = getOwnedPath(userId, pathId);

        LearningSession session = new LearningSession();
        session.setLearningPath(path);
        session.setSessionDate(request.getSessionDate());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setNotes(request.getNotes());

        LearningSession saved = learningSessionRepository.save(session);

        // Logging a session is a natural signal the path is now underway
        if (path.getStatus() == LearningPathStatus.NOT_STARTED) {
            path.setStatus(LearningPathStatus.IN_PROGRESS);
            learningPathRepository.save(path);
        }

        return toResponse(saved);
    }

    @Override
    public List<LearningSessionResponse> getAll(Long userId, Long pathId) {
        getOwnedPath(userId, pathId); // ownership check
        return learningSessionRepository.findByLearningPathIdOrderBySessionDateDesc(pathId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long userId, Long pathId, Long sessionId) {
        getOwnedPath(userId, pathId);
        LearningSession session = learningSessionRepository.findByIdAndLearningPathId(sessionId, pathId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        learningSessionRepository.delete(session);
    }

    private LearningPath getOwnedPath(Long userId, Long pathId) {
        return learningPathRepository.findByIdAndUserId(pathId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found"));
    }

    private LearningSessionResponse toResponse(LearningSession session) {
        LearningSessionResponse dto = new LearningSessionResponse();
        dto.setId(session.getId());
        dto.setSessionDate(session.getSessionDate());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setNotes(session.getNotes());
        dto.setCreatedAt(session.getCreatedAt());
        return dto;
    }
}

