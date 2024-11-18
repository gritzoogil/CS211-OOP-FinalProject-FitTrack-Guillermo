package model;

import java.time.LocalDate;
import java.util.Date;

public class User {
	private int id;
	private String name;
	private int birthYear;
	private String sex;
	private double height;
	private double weight;
	private LocalDate startDate;
	private Date endDate;
	private String goal;
	
	// constructor
    public User(int id, String name, int birthYear, String sex, double height, double weight,  LocalDate startDate, Date endDate, String goal) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
    }

	// getter and setter
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getBirthYear() {
		return birthYear;
	}
	
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getGoal() {
		return goal;
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}
}
