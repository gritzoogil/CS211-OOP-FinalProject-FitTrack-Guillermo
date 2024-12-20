package service;

import java.sql.SQLException;
import java.util.Scanner;

import dao.WeightEntryDAO;
import model.WeightEntry;

public class CaloriesBurnedCalculator {
	// get activity
    public String getActivityInput(Scanner scanner) {
        String activity;
        while (true) {
            System.out.println("Select an activity: walking, cycling, running");
            activity = scanner.nextLine().toLowerCase();
            if (activity.equals("walking") || activity.equals("cycling") || activity.equals("running")) {
                break;
            } else {
                System.out.println("Invalid activity. Please choose walking, cycling, or running.");
            }
        }
        return activity;
    }

    // get pace
    public String getPaceInput(Scanner scanner) {
        String pace;
        while (true) {
            System.out.println("Select a pace: slow, normal, fast");
            pace = scanner.nextLine().toLowerCase();
            if (pace.equals("slow") || pace.equals("normal") || pace.equals("fast")) {
                break;
            } else {
                System.out.println("Invalid pace. Please choose slow, normal, or fast.");
            }
        }
        return pace;
    }

    // get the weight
    public double getWeightInput(int userId) {
        WeightEntryDAO weightEntryDAO = new WeightEntryDAO();
        try {
            WeightEntry latestWeightEntry = weightEntryDAO.getCurrentWeightByUserId(userId);
            if (latestWeightEntry != null) {
                return latestWeightEntry.getWeight(); // get the most recent weight
            } else {
                System.out.println("No weight entries found for this user.");
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching weight data: " + e.getMessage());
            return 0;
        }
    }

    // get duration
    public int getDurationInput(Scanner scanner) {
        int durationMinutes = 0;
        while (true) {
            System.out.println("Enter the duration of the activity in minutes:");
            if (scanner.hasNextInt()) {
                durationMinutes = scanner.nextInt();
                if (durationMinutes > 0) {
                    break;
                } else {
                    System.out.println("Duration must be a positive number. Try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a whole number.");
                scanner.next(); // clear invalid
            }
        }
        return durationMinutes;
    }

    // get MET
    public double getMetValue(String activity, String pace) {
        switch (activity) {
            case "walking":
                return switch (pace) {
                    case "slow" -> 2.8;
                    case "normal" -> 3.5;
                    case "fast" -> 4.8;
                    default -> 0;
                };
            case "cycling":
                return switch (pace) {
                    case "slow" -> 4.0;
                    case "normal" -> 6.8;
                    case "fast" -> 8.5;
                    default -> 0;
                };
            case "running":
                return switch (pace) {
                    case "slow" -> 7.0;
                    case "normal" -> 9.8;
                    case "fast" -> 11.5;
                    default -> 0;
                };
            default:
                return 0;
        }
    }

    // calculate
    public int calculateCalories(String activity, String pace, double weight, int durationMinutes) {
        int met = (int) getMetValue(activity, pace);
        double durationHours = durationMinutes / 60.0;
        return (int) (met * weight * durationHours);
    }
}