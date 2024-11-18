CREATE DATABASE IF NOT EXISTS fittrack;

CREATE TABLE IF NOT EXISTS `User` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    birth_year INT NOT NULL,
    start_date DATE,
    end_date DATE,
    goal ENUM('lose', 'gain', 'maintain') NOT NULL
);

CREATE TABLE IF NOT EXISTS WeightEntry (
    user_id INT,
    `date` DATE,
    weight DECIMAL(5, 2),
    PRIMARY KEY (user_id, `date`),
    FOREIGN KEY (user_id) REFERENCES `User`(id)
);

CREATE TABLE IF NOT EXISTS CalorieEntry (
    user_id INT,
    `date` DATE,
    calories INT,
    proteins INT,
    carbs INT,
    fats INT,
    PRIMARY KEY (user_id, `date`),
    FOREIGN KEY (user_id) REFERENCES `User`(id)
);
