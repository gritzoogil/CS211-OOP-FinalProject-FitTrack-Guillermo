package model;

import java.util.Date;

public class CalorieEntry {
	private int userId;
	private Date date;
	private int calories;
    private int proteins;
    private int carbs;
    private int fats;
    
    // constructor
    public CalorieEntry(int userId, Date date, int calories, int proteins, int carbs, int fats) {
        this.userId = userId;
        this.date = date;
        this.calories = calories;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fats = fats;
    }

	// getters and setters
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getCalories() {
		return calories;
	}
	
	public void setCalories(int calories) {
		this.calories = calories;
	}
	
	public int getProteins() {
		return proteins;
	}
	
	public void setProteins(int proteins) {
		this.proteins = proteins;
	}
	
	public int getCarbs() {
		return carbs;
	}
	
	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}
	
	public int getFats() {
		return fats;
	}
	
	public void setFats(int fats) {
		this.fats = fats;
	}
}
