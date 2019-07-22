package com.exercise.highscore;

@FunctionalInterface
public interface Predicate<T> {

    public boolean test(T t);
}
