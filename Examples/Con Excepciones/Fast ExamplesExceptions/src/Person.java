
public class Person {
	
	 int age;
	 String name;
	 String lastName;
	 Person bestFriend;
	
	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public Person getBestFriend() {
		return bestFriend;
	}

	Person(int age,String name,String lastName){
		this.age = age;
		this.name = name;
		this.lastName = lastName;
	}
	
	Person(int age,String name,String lastName,Person bestFriend){
		this.age = age;
		this.name = name;
		this.lastName = lastName;
		this.bestFriend = bestFriend;
	}

	public String imprime() {
		Person p=null;
		
		calculaAGE(p);
		
		
		System.out.println("*************** Person ********************");
		System.out.println("Age: " + age);
		System.out.println("Name: " + name);
		System.out.println("Last name: " + lastName);
		//if (bestFriend!=null) System.out.println("My best friend is: " + bestFriend.name);
		System.out.println("*******************************************");
	    return "hecho";
	
	}

	private void calculaAGE(Person p) {
		// TODO Auto-generated method stub
		p.age += 1;
	}

	
	
	
}
