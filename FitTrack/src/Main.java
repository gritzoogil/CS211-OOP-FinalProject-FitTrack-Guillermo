import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dao.CalorieEntryDAO;
import dao.UserDAO;
import dao.WeightEntryDAO;
import model.CalorieEntry;
import model.User;
import model.WeightEntry;
import service.CalorieCalculator;
import service.CaloriesBurnedCalculator;
import util.DatabaseConnection;

public class Main {
	private static final Scanner SCANNER = new Scanner(System.in);
	private static final LocalDate TODAY_DATE = LocalDate.now();
	
	public static void main(String[] args) {
		// set up the database
		DatabaseConnection.setupDatabase();
		
		// instances
		UserDAO userDAO = new UserDAO();
		CalorieEntryDAO calorieEntryDAO = new CalorieEntryDAO();
		WeightEntryDAO weightEntryDAO = new WeightEntryDAO();
		
		boolean exit = false;
		
		System.out.println("Welcome to Fittrack: Calorie and Weight Manager!");
		System.out.println("Today is: " + TODAY_DATE);
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
				addUser(userDAO, calorieEntryDAO);
				break;
			case 2:
				addWeightEntry(weightEntryDAO, userDAO);
				break;
			case 3:
				addActivity(calorieEntryDAO, userDAO);
				break;
			case 4:
				viewWeightHistory(weightEntryDAO, userDAO);
				break;	
			case 5:
			    viewCaloriesAndMacros(calorieEntryDAO, userDAO);
				break;
			case 6:
				exit = true;
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}
	
	// choice 1: add user
	private static void addUser(UserDAO userDAO, CalorieEntryDAO calorieEntryDAO) {
		// user name
		System.out.println("Enter user's name: ");
		String name= SCANNER.nextLine().trim();
		
		// user sex
		String sex;
		while (true) {
			System.out.println("Enter sex (male/female): ");
			sex = SCANNER.nextLine().trim().toLowerCase();
			if (sex.equals("male") || sex.equals("female")) {
				break;
			} else {
				System.out.println("Invalid input. Please enter 'male' or 'female'.");
			}
		}
		
		// user birth year
		int birthYear = getBirthYear("Enter birth year (e.g., 1999): ");
			
		// user height
		double height = getDoubleInput("Enter height (in cm): ");
		
		// user weight
		double weight = getDoubleInput("Enter starting weight (in kg): ");
		
		// user goal
		System.out.println("Enter a goal (lose, gain, maintain): ");
		String goal= SCANNER.nextLine().trim().toLowerCase();
		if (!goal.equals("lose") && !goal.equals("gain") && !goal.equals("maintain")) {
            System.out.println("Invalid goal. Must be 'lose', 'gain', or 'maintain'.");
            return;
        }
        
		// user diet start date = today
		LocalDate startDate = TODAY_DATE;
		
		// user diet end date
		Date endDate = getDateInput("Enter diet end date (YYYY-MM-DD): ");
		
		// store to 'User' database
		User user = new User(0, name, birthYear, sex, height, weight, startDate, endDate, goal); // create User object
		try {
			// add user to database
			UserDAO.addUser(user);
			System.out.println("User added successfully.");
			
			// calculate starting calories
			int age = calculateAge(birthYear);
			double bmr = CalorieCalculator.calculateBMR(sex, age, weight, height);
			double calories = CalorieCalculator.startingCalories(bmr, goal);
			int [] macros = CalorieCalculator.calculateMacros(calories);
			
			// log starting calories
			CalorieEntry calorieEntry = new CalorieEntry(
					user.getId(), 
					startDate, 
					(int) calories, 
					macros[1], 
					macros[0], 
					macros[2]);
			CalorieEntryDAO.addCalorieEntry(calorieEntry);
			System.out.println("Calorie entry added successfully.");
			
			// log starting weight
	        Date date = Date.from(TODAY_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()); // convert TODAY_DATE to Date
			WeightEntry weightEntry = new WeightEntry(user.getId(), date, weight);
			WeightEntryDAO.addWeightEntry(weightEntry);
			System.out.println("Weight entry added successfully.\n");
			
		} catch (Exception e) { 
			System.err.println("An error occrued while adding the user.");
			e.printStackTrace();
		}
	}
	
	// choice 2: add weight entry
	private static void addWeightEntry(WeightEntryDAO weightEntryDAO, UserDAO userDAO) {
		// input name
		String name = getNameInput("Enter user's name: ");
		int userId = userDAO.getUserIdByName(name); // get id
		if (userId == -1) {
			return;
		}
		
		// input date
		Date date = getDateInput("Enter date (YYYY-MM-DD): ");
		// input weight
		double weight = getDoubleInput("Enter weight: ");
		
		WeightEntry entry = new WeightEntry(userId, date, weight);
		try {
			WeightEntryDAO.addWeightEntry(entry);
			System.out.println("Weight entry for user '" + name + "' added sucessfully.\tDate: '" + date + "'.\n");
		} catch (SQLException e) {
			System.err.println("An error occured while adding the weight entry.\n");
			e.printStackTrace();
		}
	}
	
	// choice 3: add activity
	private static void addActivity(CalorieEntryDAO calorieEntryDAO, UserDAO userDAO) {
		// input user name
	    String name = getNameInput("Enter user name: ");
	    int userId = userDAO.getUserIdByName(name); // get id
	    
	    if (userId != -1) {
	    	// get activity
	    	CaloriesBurnedCalculator calculator = new CaloriesBurnedCalculator();
	    	String activity = calculator.getActivityInput(SCANNER);
	    	
	    	// get pace
	    	String pace = calculator.getPaceInput(SCANNER);
	    	
	    	// get weight
	    	double weight = calculator.getWeightInput(userId);
	    	
	    	if (weight == 0) {
	    		System.out.println("Could not retrieve weight. Exiting activity input.");
	    		return;
	    	}
	    	
	    	// get duration
	    	int duration = calculator.getDurationInput(SCANNER);
	    	
	    	// get MET
	    	double metValue = calculator.getMetValue(activity, pace);
	    	
	    	// calculate calories burned
	    	int caloriesBurned = (int) (metValue * weight * (duration / 60.0));
	    	
	    	// get calories for today
	    	int caloriesToday = 0;
			try {
				caloriesToday = CalorieEntryDAO.getCaloriesByUserId(userId);
			} catch (SQLException e) {
				e.printStackTrace();
			}  
	    	int newCalories = caloriesToday + caloriesBurned; // add calories burned to calories for the day
	    	int[] macros = CalorieCalculator.calculateMacros(newCalories);	// calculate new macros
	    	
	    	// log new calories and macros
        	try {
				CalorieEntryDAO.updateCalorieEntryValues(
					userId, 
					newCalories,
					macros[1],
					macros[0],
					macros[2]
				);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	
	    	// show calories burned
	    	try {
	    		System.out.println("Activity logged: " + activity + " at " + pace + " pace for " + duration + " minutes.");
	    		System.out.println("Great! You can eat an additional " + caloriesBurned + " Kcals. Input '5' to check your updated \ncalories and macros for today.\n");
	    	} catch (Exception e) {
	            System.err.println("An error occurred while logging the activity: " + e.getMessage());
	        }
	    } else {
	    	System.out.println("User not found. Please try again.");
	    }
	}
	
	// choice 4: view weight history of user
	public static void viewWeightHistory(WeightEntryDAO weightEntryDAO, UserDAO userDAO) {
		// input user name
	    String name = getNameInput("Enter user name: ");
	    int userId = userDAO.getUserIdByName(name); // get id

	    if (userId != -1) {  
	        try {
	            List<WeightEntry> weightEntries = weightEntryDAO.getWeightEntriesByUserId(userId);  // get weight history
	            if (weightEntries.isEmpty()) {
	                System.out.println("No weight entries found for user '" + name + "'.\n");
	            } else {
	                System.out.println("Weight history for user '" + name + "':");
	                for (WeightEntry entry : weightEntries) {
	                    System.out.println("Date: " + entry.getDate() + "\tWeight: " + entry.getWeight());
	                }
	                System.out.println("\n\n");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error retrieving weight history: " + e.getMessage());
	            e.printStackTrace();
	        }
	    } else {
	        System.out.println("User not found. Please try again.");
	    }
	}
	
	// choice 5: view calories and macros of user for today
	public static void viewCaloriesAndMacros(CalorieEntryDAO calorieEntryDAO, UserDAO userDAO) {
		// input user name
        String name = getNameInput("Enter user name: ");
        int userId = userDAO.getUserIdByName(name);// get id
        
        if (userId != -1) {
        	try {
        		LocalDate latestDate = calorieEntryDAO.getLatestDateByUserId(userId);
        		if (TODAY_DATE.equals(latestDate.plusDays(1))) { // if no calories for today
        			int calories = CalorieCalculator.adjustCalories(userId);
        			System.out.println(calories);
        	    	int[] macros = CalorieCalculator.calculateMacros(calories);
        	    	
        	    	// log calories and macros (if updated)
                	try {
            			CalorieEntry calorieEntry = new CalorieEntry(
            				userId, 
            				latestDate.plusDays(1), 
            				(int) calories, 
            				macros[1], 
            				macros[0], 
            				macros[2]);
            			CalorieEntryDAO.addCalorieEntry(calorieEntry);
        			} catch (SQLException e) {
        				e.printStackTrace();
        			}
                	
        		}
        		
        		CalorieEntry currentCalories = calorieEntryDAO.getCalorieEntryForTodayByUserId(userId);
        		if (currentCalories != null) {
                    System.out.println("Most recent calorie and macro details for user '" + name + "':");
                    System.out.println("Date: " + currentCalories.getDate());
                    System.out.println("Calories: " + currentCalories.getCalories() + " kcals");
                    System.out.println("Proteins: " + currentCalories.getProteins() + "g");
                    System.out.println("Carbs: " + currentCalories.getCarbs() + "g");
                    System.out.println("Fats: " + currentCalories.getFats() + "g\n");
        		} else {
                    System.out.println("No calorie entries found for user '" + name + "'.\n");
        		}
        	} catch (SQLException e) {
        		System.err.println("Error retrieving weight history: " + e.getMessage());
	            e.printStackTrace();
        	}
        }
	}
	
	// helpers
	private static int calculateAge(int birthYear) {
		int currentYear = TODAY_DATE.getYear();
		return currentYear - birthYear;
	}
	
	// INPUT VALIDATION
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                SCANNER.nextLine(); 
            }
        }
    }
    
    private static int getBirthYear(String prompt) {
    	int currentYear = TODAY_DATE.getYear();
    	
        while (true) {
            System.out.print(prompt);
            String yearStr = SCANNER.nextLine().trim();
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
    
    private static double getDoubleInput(String prompt) {
    	while (true) {
    		System.out.println(prompt);
    		try {
    			double value = SCANNER.nextDouble();
    			SCANNER.nextLine();
    			return value;
    		} catch (InputMismatchException e) {
    			System.out.println("Invalid input. Please enter a decimal number.");
    			SCANNER.nextLine();
    		}
    	}
    }
    
    private static Date getDateInput(String prompt) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	dateFormat.setLenient(false);
    	while (true) {
    		System.out.print(prompt);
    		String dateStr = SCANNER.nextLine().trim();
    		try {
    			java.util.Date date = dateFormat.parse(dateStr);
    			return new Date(date.getTime());		
    		} catch (ParseException e) {
    			System.out.println("Invalid date format. Please enter in 'YYYY-MM-DD' format.");
    		}
    	}
    }
    
    private static String getNameInput(String prompt) {
        String name = "";
        String query = "SELECT COUNT(*) FROM user WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            while (true) {
                System.out.print(prompt);
                name = SCANNER.nextLine().trim();

                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, name);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        return name;
                    } else {
                        System.out.println("User not found. Please try again.");
                    }
                } catch (SQLException e) {
                    System.err.println("Error checking user existence: " + e.getMessage());
                    e.printStackTrace();
                    return null; 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}