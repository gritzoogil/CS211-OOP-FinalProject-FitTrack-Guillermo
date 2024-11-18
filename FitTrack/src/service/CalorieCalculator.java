package service;

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
}
