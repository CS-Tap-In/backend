package com.cstapin.quiz.persistence;

import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_participants")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizParticipantsEntity extends AbstractEntity {

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "correct_count", nullable = false)
    private int correctCount;

    @Builder
    public QuizParticipantsEntity(Long id,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  String username,
                                  String phoneNumber,
                                  int correctCount) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.correctCount = correctCount;
    }
}
