import com.thoughtworks.xstream.XStream;

public class Application {
	
	public static void main(String[] args){
		
		XStream xStream = new XStream();
		Person person = new Person(1, "","");
		System.out.print(xStream.toXML(person));
		
	}

}
