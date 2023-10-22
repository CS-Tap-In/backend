package com.cstapin.quiz.domain;

public enum LearningStatus {
    NONE,
    SUCCESS(NONE),
    FAILURE(NONE),
    RECOVERY(FAILURE);

    private LearningStatus updatableStatus;

    LearningStatus(LearningStatus updatableStatus) {
        this.updatableStatus = updatableStatus;
    }

    LearningStatus() {
    }

    public boolean isUpdatable(LearningStatus status) {
        return updatableStatus.equals(status);
    }
}
