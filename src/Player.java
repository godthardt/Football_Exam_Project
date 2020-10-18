

import java.io.Serializable;
import java.sql.Date;

public class Player extends Person implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
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
