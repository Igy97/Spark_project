package main;

import java.util.ArrayList;

public class Person 
{
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	ArrayList<Language> language;
	public Person(int id, String firstName, String lastName, String email, String gender,
			ArrayList<Language> language) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.language = language;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public ArrayList<Language> getLanguage() {
		return language;
	}
	public void setLanguage(ArrayList<Language> language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", gender=" + gender + ", language=" + language + "]";
	}
	
	
	
	
}
