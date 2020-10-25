package dataHandlingClasses;


import java.io.Serializable;
import java.sql.Date;

public class Person implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	protected Date birthDate;
	protected static int nextId = 0;
	protected String name;
	public String getName() {return name; }; 
	protected  int id;
	public int getId() { return id;}
	
	
}
