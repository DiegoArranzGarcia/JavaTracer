
public class Person {
	
	int age;
	String name;
	String lastName;
	Person bestFriend;
	
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

	public void imprime() {
		System.out.println("*************** Person ********************");
		System.out.println("Age: " + age);
		System.out.println("Name: " + name);
		System.out.println("Last name: " + lastName);
		//if (bestFriend!=null) System.out.println("My best friend is: " + bestFriend.name);
		System.out.println("*******************************************");
	}

}
