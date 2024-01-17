package com.cstapin.quiz.domain;

import com.cstapin.quiz.persistence.QuizParticipantsEntity;
import com.cstapin.support.domain.AbstractDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuizParticipants extends AbstractDomain {

    private final String username;
    private final String phoneNumber;
    private final int correctCount;

    @Builder
    public QuizParticipants(Long id,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt,
                            String username,
                            String phoneNumber,
                            int correctCount) {
        super(id, createdAt, updatedAt);
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.correctCount = correctCount;
    }

    public static QuizParticipants from(QuizParticipantsEntity quizParticipantsEntity) {
        return QuizParticipants.builder()
                .id(quizParticipantsEntity.getId())
                .createdAt(quizParticipantsEntity.getCreatedAt())
                .updatedAt(quizParticipantsEntity.getUpdatedAt())
                .username(quizParticipantsEntity.getUsername())
                .phoneNumber(quizParticipantsEntity.getPhoneNumber())
                .correctCount(quizParticipantsEntity.getCorrectCount())
                .build();
    }

    public QuizParticipants update(QuizParticipants newQuizParticipants) {
        if (newQuizParticipants.isCorrectCountGreater(correctCount)) {
            return QuizParticipants.builder()
                    .id(id)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .phoneNumber(phoneNumber)
                    .username(username)
                    .correctCount(newQuizParticipants.correctCount)
                    .build();
        }
        return this;
    }

    public QuizParticipantsEntity toQuizParticipantsEntity() {
        return QuizParticipantsEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .phoneNumber(phoneNumber)
                .username(username)
                .correctCount(correctCount)
                .build();
    }

    public boolean isCorrectCountGreater(int correctCount) {
        return this.correctCount > correctCount;
    }

    public String getMaskedPhoneNumber() {
        return phoneNumber.substring(0, 3) + "-****-" + phoneNumber.substring(7);
    }

    public String getMaskedUsername() {
        if (username.length() < 3) {
            return username.substring(0, 1) + "*";
        }
        return username.substring(0, 1) + "*".repeat(username.length() - 2) +
                username.substring(username.length() - 1);
    }
}
