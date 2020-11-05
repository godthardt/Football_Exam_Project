
import java.io.*;

public class Serialize {
	// Source: http://javabog.dk/OOP/kapitel18.jsp
	public static void save(Object obj, String filename) throws IOException
	  {
	    FileOutputStream datastream = new FileOutputStream(filename);
	    ObjectOutputStream objectstream = new ObjectOutputStream(datastream);
	    objectstream.writeObject(obj);
	    objectstream.close();
	  }

	  public static Object load(String filename) throws Exception
	  {
	    FileInputStream datastream = new FileInputStream(filename);
	    ObjectInputStream objectstream = new ObjectInputStream(datastream);
	    Object obj = objectstream.readObject();
	    objectstream.close();
	    return obj;
	  }
	
}
