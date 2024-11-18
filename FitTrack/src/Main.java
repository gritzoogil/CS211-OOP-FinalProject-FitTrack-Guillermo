import java.awt.image.AbstractMultiResolutionImage;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import dao.CalorieEntryDAO;
import dao.UserDAO;
import dao.WeightEntryDAO;
import model.User;
import model.WeightEntry;
import service.CalorieCalculator;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		UserDAO userDAO = new UserDAO();
		WeightEntryDAO weightEntryDAO = new WeightEntryDAO();
		CalorieEntryDAO calorieEntryDAO = new CalorieEntryDAO();
		boolean exit = false;
		
		System.out.println("Welcome to Fittrack: Calorie and Weight Manager!");
		while (!exit) {
			System.out.println("Please choose an option:");
			System.out.println("1. Add a new user");
			System.out.println("2. Add weight entry for a user");
			System.out.println("3. Add activity for a user");
			System.out.println("4. View weight history for a user");
			System.out.println("5. View calories and macros for a user");
			System.out.println("6. Exit");
			
			int choice = getIntInput("Enter your choice: ");
			switch (choice) {
			case 1:
				addUser(userDAO);
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				exit = true;
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
		
		scanner.close();
	}
	
	private static void addUser(UserDAO userDAO) {
		
		// user name
		System.out.println("Enter user's name: ");
		String name = scanner.nextLine().trim();
		
		// user sex
		String sex;
		while (true) {
			System.out.println("Enter sex (male/female): ");
			sex = scanner.nextLine().trim().toLowerCase();
			if (sex.equals("male") || sex.equals("female")) {
				break;
			} else {
				System.out.println("Invalid input. Please enter 'male' or 'female'.");
			}
		}
		  
		// user birthdate
		int birthYear = getYearInput("Enter birth year (e.g., 1999): ");
		
		
		// user height and weight
		double height = getDoubleInput("Enter height (in cm): ");
		double weight = getDoubleInput("Enter starting weight (in kg): ");
		
		// user goal
		System.out.println("Enter a goal (lose, gain, maintain): ");
		String goal = scanner.nextLine().trim().toLowerCase();
        if (!goal.equals("lose") && !goal.equals("gain") && !goal.equals("maintain")) {
            System.out.println("Invalid goal. Must be 'lose', 'gain', or 'maintain'.");
            return;
        }
        
        // user diet start date (automatically set to when the user added themself)
        LocalDate startDate = LocalDate.now();
        
        // user diet end date
        Date endDate = getDateInput("Enter diet end date (YYYY-MM-DD): ");
        
        // storing to "User" database
        User user = new User(0, name, birthYear, sex, height, weight, startDate, endDate, goal);
        try {
        	UserDAO.addUser(user);
        	
        	// call CalorieCalculator to calculate the calories and macros
        	int age = calculateAge(birthYear);
        	double bmr = CalorieCalculator.calculateBMR(sex, age, weight, height);
        	double calories = CalorieCalculator.startingCalories(bmr, goal);
        	int[] macros = CalorieCalculator.calculateMacros(calories);
        } catch (Exception e) {
        	System.err.println("An error occured while adding the user.");
        	e.printStackTrace();
        }
	}
	
//	private static void addWeightEntry(WeightEntryDAO weightEntryDAO) {
//		String name = ("Enter user's name: ");
//		Date date = getDateInput("Enter date(YYYY-MM-DD): ");
//		double weight = getDoubleInput("Enter weight: ");
//		
//		WeightEntry entry = new WeightEntry(name, date, weight);
//		try {
//			weightEntryDAO.addWeightEntry(entry);
//			System.out.println("Weight entry for user '" + name + "' added sucessfully.");
//		} catch (SQLException e) {
//			System.err.println("An error occured while adding the weight entry.");
//			e.printStackTrace();
//		}
//		
//	}
	
	// UTILS
	private static int calculateAge(int birthYear) {
		int currentYear = LocalDate.now().getYear();
		return currentYear - birthYear;
	}
	
	// INPUT VALIDATION
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.nextLine(); 
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
    	while (true) {
    		System.out.println(prompt);
    		try {
    			double value = scanner.nextDouble();
    			scanner.nextLine();
    			return value;
    		} catch (InputMismatchException e) {
    			System.out.println("Invalid input. Please enter a decimal number.");
    			scanner.nextLine();
    		}
    	}
    }
    
    private static Date getDateInput(String prompt) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	dateFormat.setLenient(false);
    	while (true) {
    		System.out.print(prompt);
    		String dateStr = scanner.nextLine().trim();
    		try {
    			java.util.Date date = dateFormat.parse(dateStr);
    			return new Date(date.getTime());		
    		} catch (ParseException e) {
    			System.out.println("Invalid date format. Please enter in 'YYYY-MM-DD' format.");
    		}
    	}
    }
    
    private static int getYearInput(String prompt) {
        int currentYear = LocalDate.now().getYear();

        while (true) {
            System.out.print(prompt);
            String yearStr = scanner.nextLine().trim();
            try {
                if (yearStr.matches("\\d{4}")) {
                    int year = Integer.parseInt(yearStr);
                    if (year > currentYear) {
                        System.out.println("The birth year cannot be in the future. Please try again.");
                    } else {
                        return year;
                    }
                } else {
                    throw new NumberFormatException("Invalid year format");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid year. Please enter a valid year in 'YYYY' format.");
            }
        }
    }

}