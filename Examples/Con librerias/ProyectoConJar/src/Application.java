import com.thoughtworks.xstream.XStream;

public class Application extends XStream{
	
	public static void main(String[] args){
		
		Application n = new Application();
		Person person = new Person(1, "","");
		System.out.print(n.toXML(person));
		
	}
}
