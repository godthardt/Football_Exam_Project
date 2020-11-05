import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = 1;  //Helps class control version of serialized objects
	private static int nextId = 0;
	private String name;
	public String getName() {return name; }; 
	private  int id;
	public int getId() { return id;}
	
	public Player(String name) {
		this.name = name;
		id = nextId++;
	}
	
	public Player(int id, String name) {
		super();
		this.name = name;
		this.id = id;
		nextId++;
	}

public String toString() {
	return name;
}
}
