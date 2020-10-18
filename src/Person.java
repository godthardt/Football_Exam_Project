

import java.io.Serializable;
import java.sql.Date;

public class Person implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	protected Date birthDate;
	protected String givenName;
	protected String surName;	
	
}
