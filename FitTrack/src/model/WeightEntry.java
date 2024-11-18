package model;

import java.util.Date;

public class WeightEntry {
	private int userId;
	private Date date;
	private double weight;
	
	public WeightEntry(int userId, Date date, double weight) {
		this.userId = userId;
		this.date = date;
		this.weight = weight;
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
    
    public double getWeight() {
    	return weight;
    }
    
    public void setWeight(double weight) {
    	this.weight = weight;
    }
}
