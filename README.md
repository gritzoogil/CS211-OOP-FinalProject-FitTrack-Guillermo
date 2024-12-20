# FitTrack

## Overview
FitTrack is a fitness-tracking application designed to help users manage their health and fitness goals. It calculates personalized daily caloric needs and tracks user activities like walking, running, and biking. The goal of this program is to promote healthy habits and make fitness management easier and more effective.

---

## Application of OOP Principles
FitTrack was developed using core OOP principles to ensure a robust and maintainable codebase:

1. **Encapsulation**  
   - Key classes such as `User`, `Calories`, and `Weight` encapsulate data with methods to safely modify and access it.
   
2. **Abstraction**  
   - The `CalorieAdjustmentGoal` abstract class provides a blueprint for calorie adjustments, hiding unnecessary implementation details and focusing on essential features.
   
3. **Inheritance**  
   - Subclasses such as `GainGoal`, `LoseGoal`, and `MaintainGoal` inherit from `CalorieAdjustmentGoal`, sharing a common structure while implementing their unique logic for adjusting calorie goals.

4. **Polymorphism**  
   - The `adjustCalories` method in `CalorieAdjustmentGoal` is overridden by its subclasses, allowing the application to dynamically adjust calorie recommendations based on user-selected fitness goals.

---

## SDG Contribution
FitTrack contributes to **Sustainable Development Goal #3: Good Health and Well-being** by:
- Promoting healthy lifestyle habits through personalized fitness tracking.
- Encouraging physical activity and balanced nutrition.
- Providing tools for individuals to monitor and achieve their fitness goals, fostering both physical and mental well-being.

---

## How to Run
Follow these steps to set up and run the FitTrack application:

### Prerequisites

1. **Java Development Kit (JDK)**
   - Install [JDK 17 or higher](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **MySQL Database Server**
   - Install [MySQL Server 8 or higher](https://dev.mysql.com/downloads/mysql/).

### Steps to Run

#### 1. Clone the Repository
Clone the FitTrack project from GitHub:
```bash
git clone https://github.com/gritzoogil/CS211-OOP-FinalProject-FitTrack-Guillermo.git
```

#### 2. Database Setup
1. Update the `DatabaseConnection` class with your database credentials:
   ```properties
    private static final String USER = "your_username";  
    private static final String PASS = "your_password"; 

    BufferedReader reader = new BufferedReader(new FileReader("file_location_of_init.sql"));
   ```

#### 3. Open the Project in Eclipse and run
1. Open Eclipse and load the project directory.
2. Navigate to `src` and locate the `Main.java` file.
3. Run the program.

---
