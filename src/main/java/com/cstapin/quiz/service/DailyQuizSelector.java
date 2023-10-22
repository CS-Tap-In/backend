package com.cstapin.quiz.service;

import com.cstapin.quiz.domain.DailySelectedQuizzes;
import com.cstapin.quiz.domain.LearningRecord;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.service.query.LearningRecordQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DailyQuizSelector {

    private final LearningRecordQueryService learningRecordQueryService;

    public DailySelectedQuizzes select(Long memberId, int dailyGoal) {
        DailySelectedQuizzes dailySelectedQuizzes = new DailySelectedQuizzes();

        List<LearningRecord> latestLearningRecords = learningRecordQueryService.findLatestLearningRecords(memberId, dailyGoal * 2);

        Map<Quiz, LearningRecord> successQuizzes = latestLearningRecords.stream().filter(LearningRecord::isSuccess)
                .collect(Collectors.toMap(LearningRecord::getQuiz, learningRecord -> learningRecord));

        List<Quiz> failQuizzes = getFailQuizzes(latestLearningRecords, successQuizzes);

        int reviewQuizSize = getReviewQuizSize(dailyGoal);

        List<LearningRecord> todayQuestionLearningRecords = learningRecordQueryService.findLearningRecords(memberId, LocalDate.now());

        // 당일에 출제된 기록이 있다면 그대로 반환한다.
        if (!todayQuestionLearningRecords.isEmpty()) {
            dailySelectedQuizzes.setFirstTimeQuestionToday(false);

            List<Quiz> quizzes = latestLearningRecords.stream().map(LearningRecord::getQuiz).collect(Collectors.toList());
            for (LearningRecord learningRecord : todayQuestionLearningRecords) {
                Quiz quiz = learningRecord.getQuiz();
                if (quizzes.contains(quiz)) {
                    dailySelectedQuizzes.addReviewQuiz(quiz);
                }
                if (!quizzes.contains(quiz)) {
                    dailySelectedQuizzes.addNewQuiz(quiz);
                }
            }
            return dailySelectedQuizzes;
        }

        // 실패한 퀴즈를 daily goal의 3/4 비율 만큼 채운다.
        for (Quiz failQuiz : failQuizzes) {
            if (dailySelectedQuizzes.getTotalQuizzesSize() >= reviewQuizSize) {
                break;
            }
            dailySelectedQuizzes.addReviewQuiz(failQuiz);
        }

        // 푼적이 없는 퀴즈로 나머지를 채운다.
        List<Quiz> unSolvedQuizzes = learningRecordQueryService.findUnSolvedQuiz(memberId);
        for (Quiz quiz : unSolvedQuizzes) {
            if (dailySelectedQuizzes.getTotalQuizzesSize() >= dailyGoal) {
                break;
            }
            dailySelectedQuizzes.addNewQuiz(quiz);
        }

        // 푼적이 없는 퀴즈가 없다면 최근에 맞은 문제를 포함시킨다.
        // 맞췄던 문제 중에 가장 예전 문제를 불러오는 방식으로 개선하자.
        for (Quiz successQuiz : successQuizzes.keySet()) {
            if (dailySelectedQuizzes.getTotalQuizzesSize() >= dailyGoal) {
                break;
            }
            dailySelectedQuizzes.addReviewQuiz(successQuiz);
        }

        return dailySelectedQuizzes;
    }

    private List<Quiz> getFailQuizzes(List<LearningRecord> latestLearningRecords, Map<Quiz, LearningRecord> successQuizzes) {
        // 전에는 맞았는데 최근에는 틀린 문제인 경우 틀린 문제로 간주한다. 반대로 전에는 틀렸는데 최근에는 맞은 문제인 경우 맞은 문제로 간주한다.
        return latestLearningRecords.stream().filter(learningRecord -> {
                    if (!learningRecord.isSuccess()) {
                        if (successQuizzes.containsKey(learningRecord.getQuiz())) {
                            return successQuizzes.get(learningRecord.getQuiz()).isBefore(learningRecord);
                        }
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(LearningRecord::getCreatedAt).reversed())
                .map(LearningRecord::getQuiz).collect(Collectors.toList());
    }

    private int getReviewQuizSize(int dailyGoal) {
        return dailyGoal * 3 / 4;
    }
}
