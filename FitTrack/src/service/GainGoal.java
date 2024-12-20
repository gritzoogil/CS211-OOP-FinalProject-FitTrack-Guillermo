package service;

public class GainGoal extends CalorieAdjustmentGoal {
    @Override
    public int adjustCalories(int latestCalories, double firstWeight, double averageWeight) {
        double predictedWeight = firstWeight + 0.5;
        if (averageWeight < predictedWeight) {
            return latestCalories + 125;
        } else {
            return latestCalories;
        }
    }
}