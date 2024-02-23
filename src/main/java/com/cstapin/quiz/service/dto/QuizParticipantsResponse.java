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
    private final long rank;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;

    public QuizParticipantsResponse(Long id,
                                    String phoneNumber,
                                    String username,
                                    int correctCount,
                                    LocalDateTime updatedAt) {
        this(id, phoneNumber, username, correctCount, 0, updatedAt);
    }

    public QuizParticipantsResponse(Long id,
                                    String phoneNumber,
                                    String username,
                                    int correctCount,
                                    long rank,
                                    LocalDateTime updatedAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.rank = rank;
        this.correctCount = correctCount;
        this.updatedAt = updatedAt;
    }

    public static QuizParticipantsResponse from(QuizParticipants quizParticipants) {
        return new QuizParticipantsResponse(quizParticipants.getId(), quizParticipants.getMaskedPhoneNumber(),
                quizParticipants.getMaskedUsername(), quizParticipants.getCorrectCount(),
                quizParticipants.getUpdatedAt());
    }

    public static QuizParticipantsResponse of(QuizParticipants quizParticipants, long rank) {
        return new QuizParticipantsResponse(quizParticipants.getId(), quizParticipants.getMaskedPhoneNumber(),
                quizParticipants.getMaskedUsername(), quizParticipants.getCorrectCount(), rank,
                quizParticipants.getUpdatedAt());
    }
}
