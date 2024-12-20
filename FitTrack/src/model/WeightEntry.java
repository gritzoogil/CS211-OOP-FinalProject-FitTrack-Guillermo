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

	// getter
	public int getUserId() {
		return userId;
	}

	public Date getDate() {
		return date;
	}

	public double getWeight() {
		return weight;
	}
}
