package service;

public class LoseGoal extends CalorieAdjustmentGoal {
    @Override
    public int adjustCalories(int latestCalories, double firstWeight, double averageWeight) {
        double predictedWeight = firstWeight - 1;
        if (averageWeight > predictedWeight) {
            return latestCalories - 250;
        } else {
            return latestCalories;
        }
    }
}