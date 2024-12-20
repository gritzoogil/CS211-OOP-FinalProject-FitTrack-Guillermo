package service;

public abstract class CalorieAdjustmentGoal {
    public abstract int adjustCalories(int latestCalories, double firstWeight, double averageWeight);
}