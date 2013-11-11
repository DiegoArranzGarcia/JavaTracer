import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;


public class SavePersons {

	public static void main(String[] args) throws Exception {
	
		FileWriter file = new FileWriter("Database.xml");
		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);
		
		Person person1 = new Person(22,"Juan","S�nchez");
		Person person2 = new Person(21,"Diego","Arranz",person1);

		List<Person> list = new ArrayList<>();
		
		list.add(person1);
		list.add(person2);
		Person[] array = {person1,person2};
		
		xstream.toXML(array,file);
		
		/*List<Person> resultList = new ArrayList<>();
		resultList = (List) xstream.fromXML(new File("Database.xml"));
		for (int i=0;i<resultList.size();i++){
			Person p = resultList.get(i);
			p.imprime();
		}*/
	}

}
