package com.cstapin.quiz.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "learning_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LearningStatus status;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public LearningRecord(Long memberId, Quiz quiz, LearningStatus status, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.quiz = quiz;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static LearningRecord of(Long memberId, Quiz quiz) {
        return new LearningRecord(memberId, quiz, LearningStatus.NONE, LocalDateTime.now());
    }

    public boolean isSuccess() {
        return LearningStatus.SUCCESS.equals(status);
    }

    public boolean isBefore(LearningRecord learningRecord) {
        return createdAt.isBefore(learningRecord.createdAt);
    }

    public void updateStatus(Long memberId, LearningStatus learningStatus) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new AccessDeniedException("해당 학습기록에 대한 권한이 없습니다.");
        }
        this.status = learningStatus;
    }
}
