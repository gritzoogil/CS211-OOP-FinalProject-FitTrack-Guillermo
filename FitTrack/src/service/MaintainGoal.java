package service;

public class MaintainGoal extends CalorieAdjustmentGoal {
    @Override
    public int adjustCalories(int latestCalories, double firstWeight, double averageWeight) {
        if (averageWeight > firstWeight + firstWeight * 0.02) {
            return latestCalories - 250;
        } else if (averageWeight < firstWeight - firstWeight * 0.02) {
            return latestCalories + 250;
        } else {
            return latestCalories;
        }
    }
}