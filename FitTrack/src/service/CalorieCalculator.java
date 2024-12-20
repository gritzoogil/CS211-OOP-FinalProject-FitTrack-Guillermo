package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dao.CalorieEntryDAO;
import dao.UserDAO;
import dao.WeightEntryDAO;
import model.User;
import model.WeightEntry;

public class CalorieCalculator {
    public static double calculateBMR(String gender, int age, double weight, double height) {
        if (gender.equalsIgnoreCase("male")) {
            return 10 * weight + 6.25 * height - 5 * age + 5;
        } else if (gender.equalsIgnoreCase("female")) {
            return 10 * weight + 6.25 * height - 5 * age - 161;
        } else {
            throw new IllegalArgumentException("Invalid gender. Must be 'male' or 'female'.");
        }
    }

    public static int startingCalories(double bmr, String goal) {
        switch (goal.toLowerCase()) {
        case "lose":
            return (int) bmr - 500;
        case "gain":
            return (int) bmr + 250;
        case "maintain":
            return (int) bmr;
        default:
            throw new IllegalArgumentException("Invalid goal. Must be 'lose', 'gain', or 'maintain'. ");
        }
    }

    public static int[] calculateMacros(double calories) {
        int carbs = (int) (calories * 0.4 / 4);
        int proteins = (int) (calories * 0.3 / 4);
        int fats = (int) (calories * 0.3 / 9);
        return new int[] {carbs, proteins, fats};
    }

    public static int adjustCalories(int userId) {
        // get the weight entries from the last 7 days
        WeightEntryDAO weightEntryDAO = new WeightEntryDAO();
        List<WeightEntry> weightEntries = new ArrayList<>();

        // get the user object from db
        UserDAO userDAO = new UserDAO();
        User user = null;
        try {
            user = userDAO.getUserById(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int latestCalories = 0;
        try {
            latestCalories = CalorieEntryDAO.getCaloriesByUserIdOther(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user != null) {
            String goal = user.getGoal();
            try {
                weightEntries = weightEntryDAO.getWeightEntriesLast7Days(userId);
                weightEntries.sort(Comparator.comparing(WeightEntry::getDate));
                double firstWeight = weightEntries.get(0).getWeight();

                double totalWeight = 0;
                for (WeightEntry entry : weightEntries) {
                    totalWeight += entry.getWeight();
                }

                double averageWeight = totalWeight / weightEntries.size();

                // Choose the appropriate goal class
                CalorieAdjustmentGoal adjustmentGoal;
                switch (goal.toLowerCase()) {
                    case "lose":
                        adjustmentGoal = new LoseGoal();
                        break;
                    case "maintain":
                        adjustmentGoal = new MaintainGoal();
                        break;
                    case "gain":
                        adjustmentGoal = new GainGoal();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid goal.");
                }

                // Delegate the responsibility to the goal class
                return adjustmentGoal.adjustCalories(latestCalories, firstWeight, averageWeight);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return latestCalories;
    }
}
