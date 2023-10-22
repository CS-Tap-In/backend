package com.cstapin.quiz.service.query;

import com.cstapin.exception.notfound.LearningRecordNotFoundException;
import com.cstapin.quiz.domain.LearningRecord;
import com.cstapin.quiz.domain.LearningRecordRepository;
import com.cstapin.quiz.domain.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningRecordQueryService {

    private final LearningRecordRepository learningRecordRepository;

    public List<LearningRecord> findLatestLearningRecords(Long memberId, int size) {
        return learningRecordRepository.findLatestLearningRecords(memberId, size, LocalDate.now().atStartOfDay());
    }

    public List<Quiz> findUnSolvedQuiz(Long memberId) {
        return learningRecordRepository.findUnSolvedQuiz(memberId);
    }

    public LearningRecord findById(Long learningRecordId) {
        return learningRecordRepository.findById(learningRecordId).orElseThrow(LearningRecordNotFoundException::new);
    }

    public List<LearningRecord> findLearningRecords(Long memberId, LocalDate date) {
        return learningRecordRepository.findLearningRecords(memberId, date);
    }
}
