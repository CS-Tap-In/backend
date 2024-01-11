package com.cstapin.quiz.service;

import java.util.List;

@FunctionalInterface
public interface RandomSelector<T> {
    List<T> select(List<T> objects, int count);
}
