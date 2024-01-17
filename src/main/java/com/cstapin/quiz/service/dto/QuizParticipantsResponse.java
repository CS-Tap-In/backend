package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizParticipants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuizParticipantsResponse {
    private final Long id;
    private final String phoneNumber;
    private final String username;
    private final int correctCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;

    public QuizParticipantsResponse(Long id,
                                    String phoneNumber,
                                    String username,
                                    int correctCount,
                                    LocalDateTime updatedAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.correctCount = correctCount;
        this.updatedAt = updatedAt;
    }

    public static QuizParticipantsResponse from(QuizParticipants quizParticipants) {
        return new QuizParticipantsResponse(quizParticipants.getId(), quizParticipants.getMaskedPhoneNumber(),
                quizParticipants.getMaskedUsername(), quizParticipants.getCorrectCount(),
                quizParticipants.getUpdatedAt());
    }
}
