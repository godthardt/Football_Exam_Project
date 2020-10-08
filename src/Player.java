import java.sql.Date;

public class Player extends Person {
	public Player(String givenName, String surName) {
		this.givenName = givenName;
		this.surName = surName;
	}
	
	public Player(String givenName, String surName, Date birthDate) {
		this.givenName = givenName;
		this.surName = surName;
		this.birthDate = birthDate; 		
	}
	
}
